import React, { useState } from "react";
import { createTransaction } from "../service/transactionServiceCreate";
import "../styles/modal.css"; // ✅ Importamos estilos

const TransactionCreateModal = ({ token, onClose, onCreate }) => {
  const [formData, setFormData] = useState({
    amount: "",
    merchant: "",
    tenpistaName: "",
    date: "",
  });
  const [message, setMessage] = useState(null);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);

    const authToken = token || localStorage.getItem("authToken");

    if (!authToken) {
      setMessage("❌ No estás autenticado. Por favor, inicia sesión.");
      return;
    }

    // ✅ Validaciones antes de enviar
    if (formData.amount <= 0) {
      setMessage("❌ Las transacciones no pueden tener montos negativos o cero.");
      return;
    }

    const selectedDate = new Date(formData.date);
    const now = new Date();
    if (selectedDate > now) {
      setMessage("❌ La fecha de transacción no puede ser superior a la fecha y hora actual.");
      return;
    }

    const newTransaction = {
      ...formData,
      date: selectedDate.toISOString(), // ✅ Convertir fecha a formato ISO
    };

    const result = await createTransaction(authToken, newTransaction);
    setMessage(result.message);

    if (result.success) {
      onCreate(result.data); // ✅ Actualizar la tabla tras la creación
      onClose();
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>➕ Nueva Transacción</h2>
        {message && <p className="modal-message">{message}</p>}
        <form onSubmit={handleSubmit}>
          <label>
            Monto:
            <input type="number" name="amount" value={formData.amount} onChange={handleChange} required />
          </label>
          <label>
            Comercio:
            <input type="text" name="merchant" value={formData.merchant} onChange={handleChange} required />
          </label>
          <label>
            Tenpista:
            <input type="text" name="tenpistaName" value={formData.tenpistaName} onChange={handleChange} required />
          </label>
          <label>
            Fecha:
            <input type="datetime-local" name="date" value={formData.date} onChange={handleChange} required />
          </label>
          <button type="submit">Crear Transacción</button>
          <button type="button" onClick={onClose}>Cancelar</button>
        </form>
      </div>
    </div>
  );
};

export default TransactionCreateModal;
