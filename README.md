
## 📄 Documentación: Iniciar el Proyecto con Docker Compose

### 📌 Requisitos Previos
Antes de iniciar el proyecto, asegúrate de tener instalados los siguientes requisitos en tu máquina:

- Docker
- Docker Compose
- Git (opcional, si clonas el proyecto desde un repositorio)

####  Clonar el repositorio
#### **Importante: al clonar el repositorio tambien traera el codigo del backend, todo en uno**
```sh
git clone https://github.com/alun-dra/desafio-tenpo.git
```

### 📂 Estructura del Proyecto

```
desafio-tenpo/
│── backend-tenpo/
│── frontent-tenpo/
│── Dockerfile-backend
│── Dockerfile-frontend
│── docker-compose.yml
│── .gitignore

```
### 🚀 Cómo iniciar el proyecto
#### 1️⃣ Construir y levantar los contenedores
Ejecuta el siguiente comando desde la carpeta raíz del proyecto:

```
docker-compose up --build

```
Esto hará lo siguiente:

- Construirá las imágenes de Docker para el backend y frontend.
- Levantará los servicios de PostgreSQL, backend (Spring Boot) y frontend (Vite + Nginx).

Si quieres correr los contenedores en segundo plano (modo detached), usa:
```
docker-compose up -d --build
```

#### 📌 Servicios y Puertos

| Servicio      | Tecnología  | Puerto Local       |Descripción | 
| :--------   | :-------                           | :---------------------  |:--|
| `PostgreSQL`      | `Postgre`   | 5432      |Base de datos del sistema.|
| `Backend`      | `Spring Boot`    | 8080 | API del backend.|
| `Frontend`      | `Vite + Nginx + React` | 5371 |Aplicación web React.

#### 🔎 Verificar que los contenedores estén corriendo

Para asegurarte de que los servicios están activos, usa:
```
docker ps

```
Debes ver algo como:
```
CONTAINER ID   IMAGE                  PORTS                   NAMES
c1a2b3c4d5e6   desafio-tenpo-frontend  0.0.0.0:5173->5173/tcp desafio-tenpo-frontend-1
b2c3d4e5f6g7   desafio-tenpo-backend   0.0.0.0:8080->8080/tcp desafio-tenpo-backend-1
d3e4f5g6h7i8   postgres                0.0.0.0:5432->5432/tcp desafio-tenpo-postgres-1

```
Si ves todos los contenedores arriba, significa que el proyecto está corriendo correctamente.

#### 🌍 Acceder a la aplicación
- Frontend: http://localhost:5173
- Backend (API): http://localhost:8080
- Base de datos (si necesitas conectarte manualmente):
    - Host: localhost
    - Puerto: 5432
    - Usuario: postgres
    - Contraseña: Des_tenpo..2025
    - Base de datos: Des_tenpo

#### 🛑 Cómo detener los contenedores
Para detener los contenedores, usa:
```
docker-compose down
```
Esto detendrá y eliminará los contenedores, pero mantendrá las imágenes y volúmenes.
Si quieres eliminar todo, incluyendo volúmenes y caché de compilación, usa:
```
docker-compose down --volumes --rmi all
```

### 🛠 Solución de Problemas
#### Error: "Address already in use"
Si ves un error como:
```
Error response from daemon: Ports are already allocated

```
Es porque otro proceso está usando el puerto. Asegúrate de que ningún otro servicio esté corriendo en los puertos 5173, 8080 o 5432 antes de iniciar Docker.

Puedes verificar qué procesos están usando los puertos con:
```
netstat -ano | findstr :5173
```
Si necesitas liberar un puerto en Windows, usa:
```
taskkill /PID <PID> /F
```
(Reemplaza <PID> con el número del proceso que aparece en el comando anterior).

#### ❌ Error: "Connection refused" al acceder a http://localhost
Si el frontend o backend no carga en el navegador:

    1. Verifica que los contenedores están corriendo con docker ps.
    2. Asegúrate de que el firewall de Windows no está bloqueando el puerto.
    3. Prueba acceder con 127.0.0.1 en lugar de localhost:
        - http://127.0.0.1:5173
        - http://127.0.0.1:8080

#### 📌 Notas Adicionales

🔄 Cómo reconstruir el proyecto
Si realizas cambios en el código o en los Dockerfile, reconstruye el proyecto con:
```
docker-compose up --build

```
Si solo quieres reconstruir el frontend o backend sin afectar los otros servicios, usa:
```
docker-compose build frontend
docker-compose up frontend


```
#
#
#
#

## 🚀 Frontend Tenpo

Este proyecto es una aplicación de frontend desarrollada 
con **React + Vite**, que consume una API para gestionar
transacciones. Se utiliza **React Query** para manejar caché 
y evitar errores de límite de solicitudes (`429`).

---

## 📌 **Tecnologías utilizadas**
- ⚡ **Vite** (para un desarrollo rápido)
- ⚛️ **React** (estructura del frontend)
- 🎨 **CSS** con estilos modulares
- 📡 **Axios** (para consumo de API)
- 🛠 **React Query** (manejo de datos en caché)
- 📦 **React Icons** (iconos para UI)
- 🔐 **Autenticación con Token Bearer**

---

## 🛠 **Requisitos previos**
Antes de iniciar, asegúrate de tener instalado:
- **Node.js** (versión recomendada: `>=16.x.x`)
- **npm** o **yarn** (gestor de paquetes)

Puedes verificarlo ejecutando:
```sh
node -v   # Verifica la versión de Node.js
npm -v    # Verifica la versión de npm
```
### 🚀 Instalación
#### 1️⃣ Clonar el repositorio
#### **Importante: al clonar el repositorio tambien traera el codigo del backend, todo en uno**
**Si has clonado el proyecto en la iniciacion con docker, porfavor ignora este paso git clone https://github.com/alun-dra/desafio-tenpo.git**
```sh
git clone https://github.com/alun-dra/desafio-tenpo.git
cd frontend-tenpo
```
#### 2️⃣ Instalar las dependencias
```sh
npm install
# o si prefieres yarn
yarn install
```

### 🔥 Ejecutar en modo desarrollo
Para iniciar el servidor de desarrollo con Vite, ejecuta:
```sh
npm run dev
# o si usas yarn
yarn dev
```
### 📁 Estructura del proyecto
```sh
frontend-tenpo/
│── public/               # Archivos estáticos
│── src/                  # Código fuente
│   ├── assets/           # Imágenes y otros assets
│   ├── components/       # Componentes reutilizables
│   ├── pages/            # Páginas principales
│   ├── routes/           # Configuración de rutas
│   ├── service/          # Servicios para consumir APIs
│   ├── styles/           # Archivos CSS
│   ├── utils/            # Utilidades y validaciones
│   ├── App.jsx           # Punto de entrada de la aplicación
│   ├── main.jsx          # Renderizado principal
│── .env                  # Variables de entorno
│── package.json          # Dependencias y scripts
└── vite.config.js        # Configuración de Vite
```



#
#
#
#
## 📖 Guía para Levantar el Proyecto Localmente - Backend Tenpo

Este seccion detalla los pasos necesarios para ejecutar el proyecto Backend Tenpo en un entorno local, asegurando que la configuración de la base de datos y dependencias sean correctas.

### 🔹 1. Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de tener instalados los siguientes componentes:

| Requisito    | Versión Requerida     | Verificación del Entorno   |
| :--------    | :-------              | :------------------------- |
| `Java`       | `17`                  | java -version              |
| `Maven`      | `3.9.9`               | mvn --version              |
| `PostgreSQL` | `13+ (o Docker)`      | psql --version             |

### 🔹 2. Configuración de Variables de Entorno

El proyecto usa un archivo **.env** para gestionar la configuración de la base de datos. Antes de ejecutar el backend, asegúrate de definir las siguientes variables:

#### 📌 Crear o editar el archivo .env en la raíz del proyecto

```http
  DATABASE_URL=jdbc:postgresql://localhost:5432/backend_tenpo
  DATABASE_USERNAME=postgres
  DATABASE_PASSWORD=tu_contraseña
```
👉 Ajusta **DATABASE_URL**, **DATABASE_USERNAME** y **DATABASE_PASSWORD** según tu configuración local.

### 🔹 3. Configuración de **application.properties** para usar el **.env**

El archivo **src/main/resources/application.properties** debe leer las variables de entorno correctamente. Si no está configurado, modifícalo así:

```http
  📌 Configuración de la Base de Datos
  spring.datasource.url=${DATABASE_URL}
  spring.datasource.username=${DATABASE_USERNAME}
  spring.datasource.password=${DATABASE_PASSWORD}

  📌 Configuración de Hibernate
  spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true

```

### 🔹 4. Instalación de Dependencias
Ejecuta el siguiente comando para descargar todas las dependencias del proyecto:
```http
  mvn clean install -DskipTests
```
📌 Este comando:
- Descarga todas las librerías requeridas.
- Compila el código fuente sin ejecutar pruebas.

### 🔹 5. Ejecutar el Proyecto
Una vez configurada la base de datos y las variables de entorno, ejecuta:
```http
  mvn spring-boot:run
```

📌 Este comando:

- Inicia el servidor en http://localhost:8080/.
- Carga las configuraciones desde application.properties y el .env.

#
#
#
#
## 📖 Documentación de Comandos Maven para Construcción y Pruebas en Spring Boot

En esta seccion se explican los comandos específicos utilizados en el proyecto para compilar, empaquetar y ejecutar pruebas unitarias, con opciones avanzadas para omitir ciertas pruebas.

### 🔹 1. Construcción del Proyecto sin Ejecutar Pruebas
Comando:
```http
  mvn clean package -DskipTests
```
#### Descripción:
Este comando se usa para limpiar, compilar y empaquetar el proyecto en un .jar, sin ejecutar las pruebas unitarias.
#### Explicación de cada parte:
- clean → Borra los archivos generados previamente en target/ para garantizar una compilación limpia.
- package → Compila el código y empaqueta el proyecto en un archivo .jar dentro de target/.
- -DskipTests → Omite la ejecución de pruebas unitarias y de integración.

#### Ejemplo de salida esperada:
```http
  [INFO] Scanning for projects...
  [INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ backend-tenpo ---
  [INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) ---
  [INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) ---
  [INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) ---
  [INFO] Building jar: backend-tenpo/target/backend-tenpo-0.0.1-SNAPSHOT.jar
  [INFO] BUILD SUCCESS
```
**🎯 Resultado:** Se genera el archivo backend-tenpo-0.0.1-SNAPSHOT.jar sin ejecutar pruebas, lo que acelera la construcción.

### 2. Ejecución de Pruebas Excluyendo un Archivo Específico
Comando:
```http
  mvn test -Dtest=!BackendTenpoApplicationTests
```
#### Descripción:
Ejecuta todas las pruebas unitarias del proyecto excepto el archivo BackendTenpoApplicationTests.
#### Explicación de cada parte:
- test → Ejecuta todas las pruebas de la carpeta src/test/java/.
- -Dtest=!BackendTenpoApplicationTests → Excluye la prueba BackendTenpoApplicationTests, lo cual es útil si contiene lógica de integración que podría fallar o ser innecesaria en ciertos contextos.
#### Ejemplo de salida esperada:
```http
  [INFO] Scanning for projects...
  [INFO] --- maven-surefire-plugin:2.22.2:test (default-test) ---
  [INFO] Running com.example.backend_tenpo.TransactionServiceTest
  [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
  [INFO] Running com.example.backend_tenpo.controller.TransactionControllerTest
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
```
**🎯 Resultado:** Se ejecutan todas las pruebas excepto BackendTenpoApplicationTests, lo que puede ser útil si esta prueba es redundante o lenta.

### 🔹 Ejemplo de Uso Combinado
Si deseas construir el proyecto sin ejecutar pruebas y luego ejecutar pruebas unitarias excluyendo BackendTenpoApplicationTests, usa estos comandos en secuencia:
```http
  mvn clean package -DskipTests && mvn test -Dtest=!BackendTenpoApplicationTests
```
**📌 Esto limpiará el proyecto, construirá el** .jar y ejecutará todas las pruebas unitarias excepto BackendTenpoApplicationTests en una sola ejecución.


#
#
#
#
## 📌 Documentación de la API - Backend Tenpo

#### 📍 Base URL:

```http
  http://localhost:8080/api
```
#### 📍 🛡️ Autenticación:
Todas las rutas protegidas requieren un JWT Token en el header Authorization:
```http
  Authorization: Bearer <tu-token-jwt>

```
## 1️⃣ Autenticación y Registro

### 🔹 Registro de Usuario
#### 📍 Endpoint:

```http
  POST /api/auth/register
```
📌 Descripción: Crea una nueva cuenta de usuario.

📥 Body (JSON):

```http
{
  "username": "testUser",
  "email": "test@example.com",
  "password": "securepassword123"
}

```
📤 Respuesta (201 Created):

```http
{
  "id": 1,
  "username": "testUser",
  "email": "test@example.com",
  "password": "$2a$10$o/MQL94fPh.Re4KeSQRWme/XdqQKn/biYcFX9fDLjfmV4ADKN5L0C",
  "transactions": null
}

```
### 🔹 Inicio de Sesión
#### 📍 Endpoint:
```http
POST /api/auth/login
```
📌 Descripción: Autentica un usuario y devuelve un JWT Token.

📥 Body (JSON):
```http
{
  "username": "testUser",
  "password": "securepassword123"
}

```
📤 Respuesta (200 OK):
```http
{
  "token": "eyJhbGciOiJIUzI1..."
}
```
📌 Este token se debe usar en los siguientes requests como:
```http
Authorization: Bearer <token>
```


## 2️⃣ Transacciones
### 🔹 Crear una Transacción
#### 📍 Endpoint:
```http
POST /api/transactions
```
📌 Descripción: Registra una nueva transacción para el usuario autenticado.

🔑 Headers Requeridos:
```http
Authorization: Bearer <token>
```
📥 Body (JSON):
```http
{
  "amount": 6000,
  "merchant": "Supermercado",
  "tenpistaName": "Juan Pérez",
  "date": "2024-02-10T12:00:00"
}
```
📤 Respuesta (200 OK):
```http
{
  "id": 1,
  "amount": 6000,
  "merchant": "Supermercado",
  "tenpistaName": "Juan Pérez",
  "date": "2024-02-10T12:00:00"
}
```
### 🔹 Obtener Transacciones del Usuario (Paginadas)
#### 📍 Endpoint:
```http
GET /api/transactions
```
📌 Descripción: Recupera todas las transacciones del usuario autenticado, paginadas.

🔑 Headers Requeridos:
```http
Authorization: Bearer <token>
```
📥 Parámetros Query:

| Parámetro | Type     | Description                      |
| :-------- | :------- | :-------------------------       |
| `page`    | `int`    | **Número de página**. (ej. 0)    |
| `size`    | `int`    | **Tamaño de la página**. (ej. 0) |


📌 Ejemplo de Request:
```http
GET /api/transactions?page=0&size=7

```
📤 Respuesta (200 OK - Paginada):
```http
{
  "content": [
    {
      "id": 1,
      "amount": 6000,
      "merchant": "Supermercado",
      "tenpistaName": "Juan Pérez",
      "date": "2024-02-10T12:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 7
  },
  "totalPages": 1,
  "totalElements": 1
}
```
### 🔹 Actualizar una Transacción
#### 📍 Endpoint:
```http
PUT /api/transactions/{id}
```
📌 Descripción: Modifica una transacción específica.

🔑 Headers Requeridos:
```http
Authorization: Bearer <token>
```
📥 Body (JSON):
```http
{
  "amount": 5000,
  "merchant": "Tienda Online",
  "tenpistaName": "Pedro Gómez",
  "date": "2024-02-11T14:30:00"
}
```
📤 Respuesta (200 OK):
```http
{
  "id": 1,
  "amount": 5000,
  "merchant": "Tienda Online",
  "tenpistaName": "Pedro Gómez",
  "date": "2024-02-11T14:30:00"
}
```

### Eliminar una Transacción
#### 📍 Endpoint:
```http
DELETE /api/transactions/{id}
```
📌 Descripción: Elimina una transacción específica.

🔑 Headers Requeridos:
```http
Authorization: Bearer <token>
```
📤 Respuesta (200 OK):
```http
"Transaction deleted"
```

### 📌 Resumen Rápido


| Método      | Endpoint                           | Descripción                        |
| :--------   | :-------                           | :--------------------------------  |
| `POST`      | `/api/auth/register`               | Registrar un usuario               |
| `POST`      | `/api/auth/login`                  | Iniciar sesión y obtener token JWT |
| `POST`      | `/api/transactions`                | Crear una transacción              |
| `GET`       | `/api/transactions?page=0&size=7r` | Obtener transacciones paginadas    |
| `PUT`       | `/api/transactions/{id}`           | Actualizar una transacción         |
| `DELETE`    | `/api/transactions/{id}`           | Eliminar una transacción           |



### 📌 Cómo Usar la API (Ejemplo con cURL)
Si quieres probar las APIs desde la terminal, aquí tienes ejemplos:

✅ Registrar un usuario
```http
curl -X POST "http://localhost:8080/api/auth/register" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "testUser",
           "email": "testuser@example.com",
           "password": "securepassword123"
         }'

```
✅ Autenticarse y obtener un token

```http
curl -X POST "http://localhost:8080/api/auth/login" \
     -H "Content-Type: application/json" \
     -d '{"username": "testUser", "password": "securepassword123"}'
```
✅ Crear una transacción

```http
curl -X POST "http://localhost:8080/api/transactions" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <tu-token-jwt>" \
     -d '{
           "amount": 6000,
           "merchant": "Supermercado",
           "tenpistaName": "Juan Pérez",
           "date": "2024-02-10T12:00:00"
         }'
```


✅ Obtener transacciones paginadas

```http
curl -X GET "http://localhost:8080/api/transactions?page=0&size=7" \
     -H "Authorization: Bearer <tu-token-jwt>"
```

✅ Actualizar una transacción

```http
curl -X PUT "http://localhost:8080/api/transactions/1" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <tu-token-jwt>" \
     -d '{
           "amount": 6000,
           "merchant": "Supermercado",
           "tenpistaName": "Juan Pérez",
           "date": "2024-02-10T12:00:00"
         }'
```

✅ Eliminar una transacción
```http
curl -X DELETE "http://localhost:8080/api/transactions/1" \
     -H "Authorization: Bearer <tu-token-jwt>"
```

### 📌 Swagger UI
Swagger UI habilitado, puedes acceder a la documentación en **(Por seguridad el parametro para el Token esta desactivado, se recomienda usar POSTMAN, THUNDER o CURL para probar la APIs de "transactions")**:
```http
http://localhost:8080/swagger-ui.html
```