FROM node:18.17.1
WORKDIR /app
ENV PORT 8080
ENV DATABASE_URL 'postgresql://root:1234@34.101.198.73:5432/capstone'
ENV JWT_SECRET 'GzVJbH1gPUdRH1mKZfBhTG+REuYXJb3C/jg7Bf9NX1c='
ENV GCLOUD_PROJECT 'api-test-capstone'
ENV GCP_BUCKET_NAME 'capstn_bucket'
ENV MODEL_PATH_OBESITY 'https://storage.googleapis.com/capstn_bucket/model1/model.json'
ENV MODEL_PATH_DIABETES 'https://storage.googleapis.com/capstn_bucket/model2/model.json'
COPY . .
RUN npm install
EXPOSE 8080
CMD ["npm", "start"]