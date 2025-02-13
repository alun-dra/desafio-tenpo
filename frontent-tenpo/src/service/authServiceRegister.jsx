import axios from "axios";
import { validateUserData } from "../utils/validationRegister"; 

const API_URL = import.meta.env.VITE_AUTH_REGISTER; 

export const registerUser = async (userData) => {
  const errors = validateUserData(userData);

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

    if (response.status !== 201 && response.status !== 200) {
      throw new Error(`Error en la respuesta del servidor: ${response.status}`);
    }

    return response.data; 
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `Error del servidor: ${error.response.status} - ${error.response.data?.message || "Error desconocido"}`
        );
      } else if (error.request) {
        throw new Error("No se recibió respuesta del servidor. Verifica tu conexión.");
      } else {
        throw new Error("Error al enviar la solicitud.");
      }
    } else {
      throw new Error(error.message || "Ocurrió un error inesperado.");
    }
  }
};
