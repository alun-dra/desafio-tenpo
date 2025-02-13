import axios from "axios";

const API_URL = import.meta.env.VITE_TRANSACTION_UPDATE;

export const updateTransaction = async (token, transactionId, updatedData) => {
  try {
    if (!token) {
      throw new Error("No token found, user is not authenticated.");
    }

    console.log(`🔄 Enviando UPDATE a: ${API_URL}/${transactionId}`);
    console.log(`📦 Datos enviados:`, updatedData);

    const response = await axios.put(`${API_URL}/${transactionId}`, updatedData, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });

    console.log("✅ Transacción actualizada con éxito.", response.data);
    return { success: true, message: "Transacción actualizada con éxito.", data: response.data };

  } catch (error) {
    console.error("❌ Error actualizando la transacción:", error);

    if (error.response?.status === 429) {
      return { success: false, message: "⚠️ Demasiadas solicitudes. Espera un momento antes de intentarlo nuevamente." };
    }

    return { success: false, message: `Error actualizando la transacción: ${error.response?.data?.message || error.message}` };
  }
};
