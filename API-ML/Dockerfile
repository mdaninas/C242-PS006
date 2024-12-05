FROM python:3.9-slim
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ENV JWT_SECRET=GzVJbH1gPUdRH1mKZfBhTG+REuYXJb3C/jg7Bf9NX1c=
ENV DATABASE_URL=postgresql://root:1234@34.101.198.73:5432/capstone
ENV DIABETES_MODEL_PATH=gs://capstn_bucket/model2/diabetes_model.h5
ENV OBESITY_MODEL_PATH=gs://capstn_bucket/model1/obesity.h5
ENV GCLOUD_PROJECT_ID=api-test-capstone
ENV EXPRESS_API_URL=https://glusity-api-562817970631.asia-southeast2.run.app
ENV VERTEX_AI_LOCATION=us-central1
ENV VERTEX_AI_MODEL=gemini-1.5-pro-002
WORKDIR /app
COPY google-credentials.json /app/google-credentials.json
COPY . /app
ENV GOOGLE_APPLICATION_CREDENTIALS="/app/google-credentials.json"
RUN pip install --no-cache-dir -r requirements.txt
EXPOSE 8080
CMD ["python", "app.py"]
