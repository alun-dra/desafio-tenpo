import React, { useState } from "react";
import TransactionsTable from "../components/TransactionsTable";
import "../styles/transactions.css"; // Estilos para la página

function Transactions() {
  
  const handleEdit = (transaction) => {
    alert(`Ver detalles de la transacción ID: ${transaction.id}`);
  };

  const handleDelete = (id) => {
    const confirmDelete = window.confirm("¿Seguro que deseas eliminar esta transacción?");
    if (confirmDelete) {
      setTransactions(transactions.filter((t) => t.id !== id));
    }
  };

  return (
    <div className="transactions-container">
      <h2>📜 Transacciones</h2>
      <p>Aquí puedes ver todas tus transacciones recientes.</p>
      <div className="table-wrapper">
        <TransactionsTable  onEdit={handleEdit} onDelete={handleDelete} />
      </div>
    </div>
  );
}

export default Transactions;
