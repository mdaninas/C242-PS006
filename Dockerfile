FROM node:18.17.1
WORKDIR /app
ENV PORT 8080
ENV DATABASE_URL 'postgresql://root:1234@34.101.198.73:5432/capstone'
ENV JWT_SECRET 'GzVJbH1gPUdRH1mKZfBhTG+REuYXJb3C/jg7Bf9NX1c='
ENV GCLOUD_PROJECT 'api-test-capstone'
ENV ALLOWED_ORIGIN='https://backend-model-api-562817970631.asia-southeast2.run.app'
COPY . .
RUN npm install
EXPOSE 8080
CMD ["npm", "start"]