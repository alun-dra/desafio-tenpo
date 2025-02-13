import axios from "axios";

const API_URL = import.meta.env.VITE_TRANSACTION_GET_IN;

export const createTransaction = async (token, newTransaction) => {
  try {
    if (!token) {
      throw new Error("No token found, user is not authenticated.");
    }

    console.log(`➕ Enviando POST a: ${API_URL}`);
    console.log(`📦 Datos enviados:`, newTransaction);

    const response = await axios.post(API_URL, newTransaction, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });

    console.log("✅ Transacción creada con éxito.", response.data);
    return { success: true, message: "Transacción creada con éxito.", data: response.data };

  } catch (error) {
    console.error("❌ Error creando la transacción:", error);

    if (error.response?.status === 429) {
      return { success: false, message: "⚠️ Demasiadas solicitudes. Espera un momento antes de intentarlo nuevamente." };
    }

    return { success: false, message: `Error creando la transacción: ${error.response?.data?.message || error.message}` };
  }
};
