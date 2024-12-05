import os
import jwt
import json
import requests
import numpy as np
import tensorflow as tf
from flask import Flask, request, jsonify
from datetime import datetime
from functools import wraps
from google.cloud import storage
from tensorflow.keras import backend as K
from tensorflow.keras.saving import register_keras_serializable
from psycopg2 import connect
from psycopg2.extras import RealDictCursor
from dotenv import load_dotenv
import vertexai
from vertexai.generative_models import GenerativeModel

# Load environment variables
load_dotenv()
API_1_URL = os.getenv("API_1_URL")

# Suppress TensorFlow warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'

# Register custom activation function
@register_keras_serializable()
def custom_relu(x):
    return K.maximum(0.0, x)

# Flask app
app = Flask(__name__)

# JWT Secret Key (Should match the secret used in Express.js)
JWT_SECRET = os.environ.get('JWT_SECRET')
if not JWT_SECRET:
    raise ValueError("JWT_SECRET environment variable is required")

JWT_ALGORITHM = 'HS256'

# Database connection
DATABASE_URL = os.environ.get('DATABASE_URL')
if not DATABASE_URL:
    raise ValueError("DATABASE_URL environment variable is required")

def get_db_connection():
    return connect(DATABASE_URL, cursor_factory=RealDictCursor)

#Kode rekomendasi VertexAI 
class VertexAIRecommendationSystem:
    def __init__(self):

        project = os.environ.get('GCLOUD_PROJECT_ID')
        location = os.environ.get('VERTEX_AI_LOCATION')
        vertexai.init(project=project, location=location)


        model_name = os.environ.get('VERTEX_AI_MODEL')
        self.model = GenerativeModel(model_name)
    
    def generate_diabetes_recommendations(self, prediction_score, user_data):
        prompt = f"""
        Saya tau kamu hanya AI namun saya ingin mencoba memberikan rekomendasi kesehatan, karena saya membuat model Machine Learning untuk memprediksi resiko terkena diabetes
        Profil Risiko Diabetes:
        - Skor Risiko: {prediction_score}%
        - Usia: {user_data.get('age')}
        - BMI: {user_data.get('bmi')}
        - Gula Darah: {user_data.get('blood_glucose_level')}
        - Penyakit Jantung: {user_data.get('heart_disease')}
        - Hipertensi: {user_data.get('hypertension')}
        - Riwayat Merokok: {user_data.get('smoking_history')}
        - HbA1c: {user_data.get('hba1c')}

        Berikan rekomendasi kesehatan yang dipersonalisasi berdasarkan profil di atas.
        Berikan maksimal 3-4 poin spesifik tentang aktivitas dan kebiasaan yang dapat membantu mengurangi risiko diabetes.
        Gunakan bahasa yang ramah dan memotivasi.
        Batasi jawaban maksimal 200 kata.
        """
        
        try:
            response = self.model.generate_content(prompt)
            return response.text
        except Exception as e:
            print(f"Error generating diabetes recommendations: {e}")
            return "Maaf, saat ini kami tidak dapat memberikan rekomendasi khusus. Silakan konsultasikan dengan profesional kesehatan untuk saran yang lebih mendalam."

    def generate_obesity_recommendations(self, obesity_category, user_data):
        prompt = f"""
        Saya tau kamu hanya AI namun saya ingin mencoba memberikan rekomendasi kesehatan, karena saya membuat model Machine Learning untuk memprediksi klasifikasi obesitas
        Profil Obesitas:
        - Kategori: {obesity_category}
        - Usia: {user_data.get('age')}
        - Berat Badan: {user_data.get('weight')} kg
        - Tinggi Badan: {user_data.get('height')} cm
        - Frekuensi Konsumsi Sayur (kali/hari): {user_data.get('fcvc')}
        - Jumlah Makanan Utama Per Hari: {user_data.get('ncp')}
        - Konsumsi Air Harian (liter): {user_data.get('ch2o')}
        - Aktivitas Fisik Mingguan (jam/hari): {user_data.get('faf')}
        - Penggunaan Perangkat Teknologi (jam/hari): {user_data.get('tue')}

        Berikan saran manajemen berat badan yang tepat berdasarkan profil di atas.
        Berikan 3-4 poin spesifik seputar aktivitas dan kebiasaan untuk mencapai berat badan ideal.
        Gunakan pendekatan yang positif dan memotivasi.
        Batasi jawaban maksimal 200 kata.
        """
        
        try:
            response = self.model.generate_content(prompt)
            return response.text
        except Exception as e:
            print(f"Error generating obesity recommendations: {e}")
            return "Maaf, saat ini kami tidak dapat memberikan rekomendasi khusus. Silakan konsultasikan dengan profesional kesehatan untuk saran yang lebih mendalam."


recommendation_system = VertexAIRecommendationSystem()

# Function to create tables
def create_tables():
    try:
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                # Create diabetes_predictions table
                cursor.execute("""
                    CREATE TABLE IF NOT EXISTS diabetes_predictions (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        gender FLOAT NOT NULL,
                        age FLOAT NOT NULL,
                        height FLOAT NOT NULL,
                        weight FLOAT NOT NULL,
                        heart_disease BOOLEAN NOT NULL,
                        hypertension BOOLEAN NOT NULL,
                        smoking_history BOOLEAN NOT NULL,
                        blood_glucose FLOAT NOT NULL,
                        has_hba1c BOOLEAN NOT NULL,
                        hba1c_level FLOAT, -- Optional jika has_hba1c = 1
                        bmi FLOAT NOT NULL,
                        prediction FLOAT NOT NULL,
                        recommendation TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """)

                # Create obesity_predictions table
                cursor.execute("""
                    CREATE TABLE IF NOT EXISTS obesity_predictions (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        gender FLOAT NOT NULL,
                        age FLOAT NOT NULL,
                        height FLOAT NOT NULL,
                        weight FLOAT NOT NULL,
                        fcvc FLOAT NOT NULL,
                        ncp FLOAT NOT NULL,
                        ch2o FLOAT NOT NULL,
                        faf FLOAT NOT NULL,
                        tue FLOAT NOT NULL,
                        predictions JSON NOT NULL,
                        predicted_category VARCHAR(50) NOT NULL,
                        recommendation TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """)

                conn.commit()
                print("Tables created successfully!")

    except Exception as e:
        print(f"Error creating tables: {str(e)}")
        raise

# Function to load model from GCS
def load_model_from_gcs(gcs_path, custom_objects=None):
    """Load a TensorFlow model from Google Cloud Storage."""
    local_path = '/tmp/model.h5'
    try:
        client = storage.Client()
        bucket_name, file_path = gcs_path.replace("gs://", "").split("/", 1)
        bucket = client.get_bucket(bucket_name)
        blob = bucket.blob(file_path)
        blob.download_to_filename(local_path)
        print(f"Model downloaded to {local_path}")
        return tf.keras.models.load_model(local_path, custom_objects=custom_objects)
    except Exception as e:
        print(f"Error loading model from GCS: {str(e)}")
        raise

# Load models
def load_and_compile_models():
    try:
        diabetes_model_path = os.environ.get('DIABETES_MODEL_PATH')
        obesity_model_path = os.environ.get('OBESITY_MODEL_PATH')

        if not diabetes_model_path or not obesity_model_path:
            raise ValueError("Model paths are not set in the environment variables.")

        diabetes_model = load_model_from_gcs(
            diabetes_model_path, custom_objects={'custom_relu': custom_relu}
        )
        diabetes_model.compile(
            optimizer=tf.keras.optimizers.Adam(learning_rate=0.0001),
            loss='BinaryFocalCrossentropy',
            metrics=['accuracy']
        )

        obesity_model = load_model_from_gcs(obesity_model_path)
        obesity_model.compile(
            optimizer=tf.keras.optimizers.Adam(learning_rate=0.0001),
            loss='categorical_crossentropy',
            metrics=['accuracy']
        )

        print("Models loaded and compiled successfully!")
        return diabetes_model, obesity_model
    except Exception as e:
        print(f"Error loading models: {str(e)}")
        raise

print("Creating tables...")
create_tables()

print("Loading models...")
diabetes_model, obesity_model = load_and_compile_models()

# Utility functions
def calculate_bmi(weight, height):
    return float(weight / (height ** 2))

def calculate_hba1c(blood_glucose_level):
    return float((46.7 + blood_glucose_level) / 28.7)

def preprocess_input(input_data):
    input_array = np.array(input_data, dtype=np.float32)
    if len(input_array.shape) == 1:
        input_array = np.expand_dims(input_array, axis=0)
    return input_array

# Decorator for token validation
def is_token_blacklisted(token):
    try:
        response = requests.get(
            f"{API_1_URL}/auth/isTokenBlacklisted",  # URL API 1 dari env
            headers={'Authorization': f'Bearer {token}'}
        )
        response_data = response.json()
        return response_data.get('blacklisted', False)
    except Exception as e:
        print(f"Error checking blacklist status: {e}")
        return True 

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        if not token or not token.startswith("Bearer "):
            return jsonify({'success': False, 'error': 'Token is missing or invalid'}), 401

        token = token.split(" ")[1]
        try:
            # Verifikasi token JWT
            decoded = jwt.decode(token, JWT_SECRET, algorithms=[JWT_ALGORITHM])

            if is_token_blacklisted(token):
                return jsonify({'success': False, 'error': 'Token has been blacklisted'}), 401

            request.user = decoded  # Simpan data user dari token
        except jwt.ExpiredSignatureError:
            return jsonify({'success': False, 'error': 'Token has expired'}), 401
        except jwt.InvalidTokenError:
            return jsonify({'success': False, 'error': 'Invalid token'}), 401

        return f(*args, **kwargs)
    return decorated

@app.route('/predict/diabetes', methods=['POST'])
@token_required
def predict_diabetes():
    try:
        data = request.json
        user_id = request.user['userId']

        gender = float(data['gender']) #1 male 0 female
        age = float(data['age'])
        height = float(data['height']) / 100
        weight = float(data['weight'])
        heart_disease = bool(data['heart_disease'])
        hypertension = bool(data['hypertension'])
        smoking_history = bool(data['smoking_history'])
        blood_glucose_level = float(data['blood_glucose_level'])
        has_hba1c = bool(data['has_hba1c'])

        bmi = calculate_bmi(weight, height)
        hba1c = float(data['hba1c']) if has_hba1c else calculate_hba1c(blood_glucose_level)

        input_data = [
            gender, age, hypertension, heart_disease,
            smoking_history, bmi, hba1c, blood_glucose_level
        ]
        input_array = preprocess_input(input_data)

        try:
            prediction = float(diabetes_model.predict(input_array, verbose=0)[0][0] * 100)
        except Exception as e:
            print(f"Prediction error: {str(e)}")
            return jsonify({
                'success': False,
                'error': 'Error making prediction. Please check input values.'
            })

        recommendation = recommendation_system.generate_diabetes_recommendations(
            prediction, 
            {
                'age': age,
                'bmi': bmi,
                'blood_glucose_level': blood_glucose_level,
                'heart_disease': heart_disease,
                'hypertension': hypertension,
                'smoking_history': smoking_history,
                'hba1c': hba1c
            }
        )

        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute(
                    """
                    INSERT INTO diabetes_predictions (
                        user_id, gender, age, height, weight, heart_disease,
                        hypertension, smoking_history, blood_glucose,
                        has_hba1c, hba1c_level, bmi, prediction, recommendation
                    )
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING id;
                    """,
                    (
                        user_id, gender, age, height, weight, heart_disease,
                        hypertension, smoking_history, blood_glucose_level,
                        has_hba1c, hba1c if has_hba1c else None, bmi, prediction, recommendation
                    )
                )
                prediction_id = cursor.fetchone()['id']
                conn.commit()

        result = {
            'success': True,
            'id': prediction_id,
            'prediction': round(prediction, 2),
            'bmi': round(bmi, 2),
            'hba1c': round(hba1c, 1),
            'recommendation': recommendation
        }
        return jsonify(result)
    
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})

@app.route('/predict/obesity', methods=['POST'])
@token_required
def predict_obesity():
    try:
        data = request.json
        user_id = request.user['userId']

        gender = float(data['gender']) #1 male 0 female
        age = float(data['age'])
        height = float(data['height']) / 100
        weight = float(data['weight'])
        fcvc = float(data['fcvc'])  # Frekuensi konsumsi sayur
        ncp = float(data['ncp']) # Jumlah makanan harian
        ch2o = float(data['ch2o']) # Asupan air harian
        faf = float(data['faf']) # Aktivitas fisik mingguan
        tue = float(data['tue']) # Waktu menggunakan teknologi

        input_data = np.array([[gender, age, height, weight, fcvc, ncp, ch2o, faf, tue]], dtype=np.float32)

        try:
            prediction = obesity_model.predict(input_data, verbose=1) 
            predicted_category_index = int(np.argmax(prediction[0]))
            
            # Tentukan kategori berdasarkan indeks
            category = None
            if predicted_category_index == 0:
                category = 'Insufficient_Weight'
            elif predicted_category_index == 1:
                category = 'Normal_Weight'
            elif predicted_category_index == 2:
                category = 'Overweight'
            else:
                category = 'Obesity'
        
        except Exception as e:
            print(f"Prediction error: {str(e)}")
            return jsonify({
                'success': False,
                'error': 'Error during prediction. Please check your input values.'
            })

        recommendation = recommendation_system.generate_obesity_recommendations(    
            category, 
            {
                'age': age,
                'weight': weight,
                'height': height,
                'fcvc': fcvc,
                'ncp': ncp,
                'ch2o': ch2o,
                'faf': faf,
                'tue': tue
            }
        )

        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                predictions_dict = {
                    'predictions': prediction[0].tolist(),
                    'category_index': predicted_category_index,
                    'category': category
                }
                predictions_json = json.dumps(predictions_dict)

                cursor.execute(
                    """
                    INSERT INTO obesity_predictions (
                        user_id, gender, age, height, weight,
                        fcvc, ncp, ch2o, faf, tue,
                        predictions, predicted_category, recommendation
                    )
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING id;
                    """,
                    (
                        user_id, gender, age, height, weight, fcvc, ncp, ch2o, faf, tue,
                        predictions_json, category, recommendation
                    )
                )
                prediction_id = cursor.fetchone()['id']
                conn.commit()

        # Response following your example format
        print(prediction[0])  # Will print like [[1.4772469e-03 6.9815987e-01 2.9994276e-01 4.2005620e-04]]
        print(predicted_category_index)  # Will print the index (e.g., 1)
        print(category)  # Will print the category (e.g., Normal_Weight)

        result = {
            'success': True,
            'id': prediction_id,
            'raw_prediction': prediction[0].tolist(),
            'category_index': predicted_category_index,
            'category': category,
            'recommendation': recommendation
        }
        return jsonify(result)

    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})
    
@app.route('/predict/history', methods=['GET'])
@token_required
def predict_history():
    try:
        user_id = request.user['userId']
        
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT * FROM diabetes_predictions
                    WHERE user_id = %s
                    ORDER BY created_at DESC;
                """, (user_id,))
                diabetes_history = [dict(row) for row in cursor.fetchall()]

                cursor.execute("""
                    SELECT * FROM obesity_predictions
                    WHERE user_id = %s
                    ORDER BY created_at DESC;
                """, (user_id,))
                obesity_history = [dict(row) for row in cursor.fetchall()]

        return jsonify({
            'success': True,
            'history': {
                'diabetes_predictions': diabetes_history,
                'obesity_predictions': obesity_history
            }
        })

    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})

@app.route('/predict/history/diabetes/<int:id>', methods=['GET'])
@token_required
def diabetes_history_by_id(id):
    try:
        user_id = request.user['userId']
        
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        id, user_id, gender, age, height, weight, heart_disease,
                        hypertension, smoking_history, blood_glucose, has_hba1c,
                        hba1c_level, bmi, prediction, recommendation, created_at
                    FROM diabetes_predictions
                    WHERE id = %s AND user_id = %s;
                """, (id, user_id))
                diabetes_result = cursor.fetchone()

                if diabetes_result:
                    return jsonify({
                        'success': True,
                        'source': 'diabetes_predictions',
                        'data': dict(diabetes_result)
                    })

        return jsonify({'success': False, 'error': 'History with the given ID was not found in diabetes_predictions'})

    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})
    
@app.route('/predict/history/obesity/<int:id>', methods=['GET'])
@token_required
def obesity_history_by_id(id):
    try:
        user_id = request.user['userId']
        
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        id, user_id, gender, age, height, weight, fcvc, ncp,
                        ch2o, faf, tue, predictions, predicted_category, recommendation, created_at
                    FROM obesity_predictions
                    WHERE id = %s AND user_id = %s;
                """, (id, user_id))
                obesity_result = cursor.fetchone()

                if obesity_result:
                    return jsonify({
                        'success': True,
                        'source': 'obesity_predictions',
                        'data': dict(obesity_result)
                    })

        return jsonify({'success': False, 'error': 'History with the given ID was not found in obesity_predictions'})

    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})
    
@app.errorhandler(404)
def not_found_error(e):
    return jsonify({
        "success": False,
        "error": "The requested URL was not found on the server. Please check your spelling or try again with a valid endpoint."
    }), 404

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))

