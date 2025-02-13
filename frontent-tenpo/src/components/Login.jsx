import React, { useState, useRef } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom"; // React Router para redirección
import { registerUser } from "../service/authServiceRegister";
import { loginUser } from "../service/authServiceLogin";

import "../styles/login.css"

function Login() {
  const [isSignUpMode, setIsSignUpMode] = useState(false);
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [registerData, setRegisterData] = useState({ username: "", email: "", password: "" });
  const [loginError, setLoginError] = useState("");
  const [registerError, setRegisterError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const containerRef = useRef(null);
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  // 🔹 React Query: Manejo del Registro
  const registerMutation = useMutation({
    mutationFn: registerUser,
    onMutate: () => {
      setRegisterError("");
      setSuccessMessage("");
    },
    onSuccess: (response) => {
      setSuccessMessage("¡Registro exitoso! " + response.message);
      setRegisterData({ username: "", email: "", password: "" }); // Limpiar formulario
    },
    onError: (error) => {
      setRegisterError(error.message);
    },
    onSettled: () => {
      queryClient.invalidateQueries(["auth"]); // Invalida cualquier caché de auth
    },
  });

  // 🔹 React Query: Manejo del Login
  const loginMutation = useMutation({
    mutationFn: loginUser,
    onMutate: () => {
      setLoginError("");
      setSuccessMessage("");
    },
    onSuccess: (token) => {
      queryClient.setQueryData(["authToken"], token); // Guardar token en React Query
      localStorage.setItem("authToken", token); // Guardar en LocalStorage
      setSuccessMessage("Inicio de sesión exitoso. Redirigiendo...");
      setTimeout(() => navigate("/dashboard"), 1500); // Redirigir
    },
    onError: (error) => {
      setLoginError(error.message);
    },
  });

  // 🔄 Manejo de Inputs
  const handleChange = (e, isLogin) => {
    if (isLogin) {
      setLoginData({ ...loginData, [e.target.name]: e.target.value });
    } else {
      setRegisterData({ ...registerData, [e.target.name]: e.target.value });
    }
  };

  // 🔄 Enviar Formularios
  const handleLoginSubmit = (e) => {
    e.preventDefault();
    loginMutation.mutate(loginData);
  };

  const handleSignUpSubmit = (e) => {
    e.preventDefault();
    registerMutation.mutate(registerData);
  };

  return (
    <div className={`container ${isSignUpMode ? "sign-up-mode" : ""}`} ref={containerRef}>
      <div className="forms-container">
        <div className="signin-signup">
          {/* 🔹 Formulario de Inicio de Sesión */}
          <form className="sign-in-form" onSubmit={handleLoginSubmit}>
            <h2 className="title">Iniciar sesión</h2>

            {loginError && <p className="error-message">{loginError}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}

            <div className="input-field">
              <i className="fas fa-user"></i>
              <input
                type="text"
                name="username"
                placeholder="Username"
                value={loginData.username}
                onChange={(e) => handleChange(e, true)}
              />
            </div>
            <div className="input-field">
              <i className="fas fa-lock"></i>
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={loginData.password}
                onChange={(e) => handleChange(e, true)}
              />
            </div>

            <button type="submit" className="btn" disabled={loginMutation.isLoading}>
              {loginMutation.isLoading ? "Iniciando sesión..." : "Ingresar"}
            </button>
          </form>

          {/* 🔹 Formulario de Registro */}
          <form className="sign-up-form" onSubmit={handleSignUpSubmit}>
            <h2 className="title">Inscribirse</h2>

            {registerError && <p className="error-message">{registerError}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}

            <div className="input-field">
              <i className="fas fa-user"></i>
              <input
                type="text"
                name="username"
                placeholder="Username"
                value={registerData.username}
                onChange={(e) => handleChange(e, false)}
              />
            </div>
            <div className="input-field">
              <i className="fas fa-envelope"></i>
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={registerData.email}
                onChange={(e) => handleChange(e, false)}
              />
            </div>
            <div className="input-field">
              <i className="fas fa-lock"></i>
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={registerData.password}
                onChange={(e) => handleChange(e, false)}
              />
            </div>

            <button type="submit" className="btn" disabled={registerMutation.isLoading}>
              {registerMutation.isLoading ? "Registrando..." : "Crear Cuenta"}
            </button>
          </form>
        </div>
      </div>

      {/* Contenido lateral */}
      <div className="panels-container">
        <div className="panel left-panel">
          <div className="content">
            <h3>¿Nuevo aquí?</h3>
            <p>Únete a nuestra comunidad y descubre nuevas oportunidades.</p>
            <button className="btn transparent" onClick={() => setIsSignUpMode(true)}>
              Inscribirse
            </button>
          </div>
          <div className="image"></div>
        </div>

        <div className="panel right-panel">
          <div className="content">
            <h3>¿Uno de nosotros?</h3>
            <p>Inicia sesión para continuar disfrutando de nuestros servicios.</p>
            <button className="btn transparent" onClick={() => setIsSignUpMode(false)}>
              Iniciar sesión
            </button>
          </div>
          <div className="image"></div>
        </div>
      </div>
    </div>
  );
}

export default Login;
