# Glusity (Cloud Computing)

This capstone project will develop a diabetes management web app on Google Cloud Platform. The backend, built with Node.js, will handle core functionality, including authentication and authorization, while the frontend will use a JavaScript framework. Predictive blood sugar trend modeling will be implemented using TensorFlow.js. Development will be conducted in Visual Studio Code, with Postman for API testing and GitHub for version control. The project will include full API documentation and backup options for reliable demonstrations.

## **CC Path:**

| Name                 | Student ID   | Universitas      |
| -------------------- | ------------ | ---------------- |
| Rayhan Al Farassy    | C308B4KY3710 | Universitas Riau |
| Reza Ramadhani Putra | C308B4KY3789 | Universitas Riau |

## **Requirements & Tools:**

![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![Flask](https://img.shields.io/badge/flask-%23000.svg?style=for-the-badge&logo=flask&logoColor=white)
![Google Cloud](https://img.shields.io/badge/GoogleCloud-%234285F4.svg?style=for-the-badge&logo=google-cloud&logoColor=white)

## **Cloud Architecture:**

![Architecture](https://github.com/mdaninas/Capstone-Project/blob/main/images/architecture.png)

## **Endpoints:**

#### 1. `/login` (POST)
**Description**: Log in a user with credentials.
  
#### 2. `/register` (POST)
- **Description**: Register a new user account.

#### 3. `/logout` (POST)
- **Description**: Log out the current user.

#### 4. `/update` (PUT)
- **Description**: Update user information.

#### 5. `/profile` (GET)
- **Description**: Retrieve the authenticated user's profile.

#### 6. `/predict_diabetes` (POST)
- **Description**: Predict the likelihood of diabetes based on the provided input data.

#### 7. `/predict_obesity` (POST)
- **Description**: Predict the likelihood of obesity based on the provided input data.

## **Service in GCP:**

- **Cloud Run**: For deploying backend services.
- **Cloud Storage**: For storing machine learning models and other files.
- **Compute Engine (SQL)**: To run database services storing prediction data and user data.

## **Testing the API**
Use Postman or any API testing tool to test the endpoints. Make sure to include the `Authorization` token in the headers for secure endpoints like `/update`, `/profile`, `/logout`, `/predict_diabetes`, and `/predict_obesity`.

**Postman Documentation:** https://documenter.getpostman.com/view/39192802/2sAYBUED2L

