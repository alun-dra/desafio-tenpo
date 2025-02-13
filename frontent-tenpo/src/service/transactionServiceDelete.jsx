import axios from "axios";

const API_URL = import.meta.env.VITE_TRANSACTION_DELETE;

export const deleteTransaction = async (token, transactionId) => {
    try {
      if (!token) {
        throw new Error("No token found, user is not authenticated.");
      }
  
      
  
      const response = await axios.delete(`${API_URL}/${transactionId}`, {
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });
  
      console.log("✅ Transacción eliminada con éxito.", response.data);
      return { success: true, message: "Transacción eliminada con éxito." };
  
    } catch (error) {
      console.error("❌ Error eliminando la transacción:", error);
  
      if (error.response?.status === 429) {
        return { success: false, message: "⚠️ Demasiadas solicitudes. Espera un momento antes de intentarlo nuevamente." };
      }
  
      return { success: false, message: `Error eliminando la transacción: ${error.response?.data?.message || error.message}` };
    }
  };
  
  
