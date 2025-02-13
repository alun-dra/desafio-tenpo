import axios from "axios";
import { validateLoginData } from "../utils/validationLogin"; 

const API_URL = import.meta.env.VITE_AUTH_LOGIN;

export const loginUser = async (userData) => {
  const errors = validateLoginData(userData);
  if (errors.length > 0) {
    throw new Error(errors.join(" | "));
  }

  try {
    const response = await axios.post(API_URL, userData, {
      headers: {
        "Content-Type": "application/json",
      },
      timeout: 10000,
    });

    if (response.status === 200) {
      const token = response.data.token;
      localStorage.setItem("authToken", token); // Guardar token en LocalStorage
      
      return token;
    } else {
      throw new Error("Error en la respuesta del servidor.");
    }
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(`Error: ${error.response.status} - ${error.response.data?.message || "Error desconocido"}`);
      } else if (error.request) {
        throw new Error("No se recibió respuesta del servidor.");
      } else {
        throw new Error("Error en la solicitud.");
      }
    } else {
      throw new Error(error.message || "Ocurrió un error inesperado.");
    }
  }
};
