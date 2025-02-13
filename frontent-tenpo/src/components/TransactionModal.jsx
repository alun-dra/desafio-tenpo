import React, { useState } from "react";
import { updateTransaction } from "../service/transactionServiceUpdate";
import "../styles/modal.css"; 

const TransactionModal = ({ token, transaction, onClose, onUpdate }) => {
  const [formData, setFormData] = useState({ ...transaction });
  const [message, setMessage] = useState(null);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
  
    const authToken = token || localStorage.getItem("authToken"); // 🔥 Asegurar el token
  
    if (!authToken) {
      setMessage("❌ No estás autenticado. Por favor, inicia sesión.");
      return;
    }
  
    const result = await updateTransaction(authToken, transaction.id, formData);
    setMessage(result.message);
  
    if (result.success) {
      onUpdate(result.data);
      onClose();
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>📝 Editar Transacción</h2>
        {message && <p className="modal-message">{message}</p>}
        <form onSubmit={handleSubmit}>
          <label>
            Monto:
            <input type="number" name="amount" value={formData.amount} onChange={handleChange} />
          </label>
          <label>
            Comercio:
            <input type="text" name="merchant" value={formData.merchant} onChange={handleChange} />
          </label>
          <label>
            Tenpista:
            <input type="text" name="tenpistaName" value={formData.tenpistaName} onChange={handleChange} />
          </label>
          <button type="submit">Guardar Cambios</button>
          <button type="button" onClick={onClose}>Cancelar</button>
        </form>
      </div>
    </div>
  );
};

export default TransactionModal;
