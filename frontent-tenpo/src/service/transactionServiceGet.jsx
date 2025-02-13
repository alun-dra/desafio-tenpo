import axios from "axios";

const API_URL = import.meta.env.VITE_TRANSACTION_GET;

export const fetchTransactions = async (token, page = 0, size = 7) => {
    try {
      const authToken = token || localStorage.getItem("authToken");
  
      if (!authToken) {
        console.error("❌ No hay token disponible");
        throw new Error("No token found, user is not authenticated.");
      }
  
      
  
      const response = await axios.get(API_URL, {
        params: { page, size },
        headers: {
          "Authorization": `Bearer ${authToken}`,
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });
  
      if (!response.data) {
        throw new Error("La respuesta de la API está vacía.");
      }
  
      const { content = [], totalElements = 0, totalPages = 1 } = response.data;
  
      if (content.length === 0) {
        console.warn("⚠️ No hay transacciones disponibles.");
      }
  
      return {
        transactions: content,
        totalPages,
        totalElements,
      };
  
    } catch (error) {
      if (error.response?.status === 429) {
        console.warn("⚠️ Demasiadas peticiones. Usando caché en React Query...");
        throw new Error("Demasiadas solicitudes");
      }
  
      console.error("⚠️ Error en la API:", error.response?.data || error.message);
      return { transactions: [], totalPages: 0, totalElements: 0 }; // Evita romper el flujo en el frontend
    }
  };
  
  
  
  
  