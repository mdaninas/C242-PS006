# Glusity (Cloud Computing)

This capstone project will develop a diabetes management web app on Google Cloud Platform. The backend, built with Express.js, will handle core functionality, including authentication and authorization, while the frontend will use a JavaScript framework. Predictive blood sugar trend modeling will be implemented using a Flask API. Development will be conducted in Visual Studio Code, with Postman for API testing and GitHub for version control. The project will include full API documentation and backup options for reliable demonstrations.

## **CC Path:**

| Name                 | Student ID   | Universitas      |
| -------------------- | ------------ | ---------------- |
| Rayhan Al Farassy    | C308B4KY3710 | Universitas Riau |
| Reza Ramadhani Putra | C308B4KY3789 | Universitas Riau |

## **Requirements & Tools:**

![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![ExpressJS](https://img.shields.io/badge/Express-%23F7DF1E?style=for-the-badge&logo=javascript&logoColor=white)
![Flask](https://img.shields.io/badge/flask-%23000.svg?style=for-the-badge&logo=flask&logoColor=white)
![Google Cloud](https://img.shields.io/badge/GoogleCloud-%234285F4.svg?style=for-the-badge&logo=google-cloud&logoColor=white)

## **Cloud Architecture:**

![Architecture](https://github.com/mdaninas/Capstone-Project/blob/main/images/architecture.png)

## **Endpoints:**

1.  `/auth/register` (POST) = Register a new user account.
2.  `/auth/login` (POST) = Log in a user with credentials.
3.  `/auth/logout` (POST) = Log out the current user.
4.  `/user/profile` (GET) = Retrieve the authenticated user's profile.
5.  `/user/update` (PUT) = Update user information.
6.  `/user/trend` (GET) = Displays weight and glucose level trend data based on input.
7.  `/predict/diabetes` (POST) = Predict the likelihood of diabetes based on the provided input data.
8.  `/predict/obesity` (POST) = Predict the likelihood of obesity based on the provided input data.
9.  `/predict/history` (GET) = History of the results predicts the possibility of obesity or diabetes.

## **Service in GCP:**

- **Cloud Run**: For deploying backend services.
- **Cloud Storage**: For storing machine learning models.
- **Cloud Storage**: For storing machine learning models.
- **Compute Engine (SQL)**: To run database services storing prediction data and user data.

## **Installation Instructions**

1. Clone the repository:

   ```bash
   git clone https://github.com/mdaninas/C242-PS006
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Set up environment variables:
   Create a `.env` file with the following `.env.example` configuration:

   ```bash
   PORT="YOUR PORT"
   DATABASE_URL="YOUR DATABASE_URL"
   JWT_SECRET="YOUR JWT_SECRET"
   GCLOUD_PROJECT="YOUR GCLOUD_PROJECT"
   ALLOWED_ORIGIN="YOUR ALLOWED_ORIGIN"
   ```

4. Run the application:
   ```bash
   npm start
   ```

## **Testing the API**

Use Postman or any API testing tool to test the endpoints. Make sure to include the `Authorization` token in the headers for secure endpoints like  `/logout`, `/profile`, `/update`, `/trend`, `/predict/diabetes`, and `/predict/obesity`.

**Postman Documentation:** https://documenter.getpostman.com/view/39192802/2sAYBUED2L
