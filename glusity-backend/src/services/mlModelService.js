const tf = require('@tensorflow/tfjs-node');
const { Storage } = require('@google-cloud/storage');
const storage = new Storage();

async function loadModel() {
  const bucketName = process.env.GCP_BUCKET_NAME;
  const modelPath = process.env.MODEL_PATH;

  if (!bucketName || !modelPath) {
    throw new Error('Bucket name or model path is not defined in environment variables.');
  }

  const [files] = await storage.bucket(bucketName).getFiles({ prefix: modelPath });
  if (files.length === 0) {
    throw new Error('Model not found in Cloud Storage.');
  }

  const model = await tf.loadLayersModel(`gs://${bucketName}/${modelPath}/model.json`);
  return model;
}

module.exports = { loadModel };
