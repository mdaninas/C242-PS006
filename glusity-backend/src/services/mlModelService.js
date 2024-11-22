const tf = require('@tensorflow/tfjs-node');

let model1 = null; // Model for obesity
let model2 = null; // Model for diabetes

async function loadModel(modelType) {
  const modelPath1 = process.env.MODEL_PATH_OBESITY; 
  const modelPath2 = process.env.MODEL_PATH_DIABETES; 

  if (modelType === 'obesity') {
    if (!modelPath1) {
      throw new Error('Model path for obesity is not defined in environment variables.');
    }
    if (!model1) {
      model1 = await tf.loadLayersModel(modelPath1);
    } 
    return model1;
  }

  if (modelType === 'diabetes') {
    if (!modelPath2) {
      throw new Error('Model path for diabetes is not defined in environment variables.');
    }
    if (!model2) {
      model2 = await tf.loadLayersModel(modelPath2);
    }
    return model2;
  }

  throw new Error('Invalid model type specified. Use "obesity" or "diabetes".');
}

module.exports = { loadModel };
