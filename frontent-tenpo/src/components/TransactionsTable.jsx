import React, { useState } from "react";
import { FaEye, FaTrash, FaPlus, FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { useQueryClient } from "@tanstack/react-query";
import { useTransactions } from "../hooks/useTransactions";
import { deleteTransaction } from "../service/transactionServiceDelete";
import TransactionModal from "../components/TransactionModal";
import TransactionCreateModal from "../components/TransactionCreateModal"; // ✅ Importar modal de creación
import "../styles/table.css";

function TransactionsTable({ token }) {
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 7;
  const [message, setMessage] = useState(null);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false); // ✅ Estado para modal de creación

  const queryClient = useQueryClient();
  const { data, isLoading, error } = useTransactions(token, currentPage, pageSize);
  const { transactions = [], totalPages = 1 } = data || {};

  const handleDelete = async (transactionId) => {
    setMessage(null);
    const authToken = token || localStorage.getItem("authToken");

    if (!authToken) {
      setMessage("❌ No estás autenticado. Por favor, inicia sesión.");
      return;
    }

    const result = await deleteTransaction(authToken, transactionId);
    setMessage(result.message);

    if (result.success) {
      queryClient.invalidateQueries(["transactions"]);
    }
  };

  const handleEdit = (transaction) => {
    setSelectedTransaction(transaction);
  };

  const handleModalClose = () => {
    setSelectedTransaction(null);
  };

  const handleTransactionUpdate = () => {
    queryClient.invalidateQueries(["transactions"]);
  };

  const handleCreateModalClose = () => {
    setIsCreateModalOpen(false);
  };

  const handleTransactionCreate = () => {
    queryClient.invalidateQueries(["transactions"]);
  };

  const goToPage = (page) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  return (
    <div className="table-container">
      {/* 🔹 Botón de agregar transacción */}
      <div className="table-header">
        <button className="add-transaction-btn" onClick={() => setIsCreateModalOpen(true)}>
          <FaPlus />
        </button>
      </div>

      {message && <p className="user-message">{message}</p>}

      {isLoading && <p className="loading-message">Cargando transacciones...</p>}

      <table className="transactions-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Monto</th>
            <th>Comercio</th>
            <th>Tenpista</th>
            <th>Fecha</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {transactions.length > 0 ? (
            transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td>{transaction.id}</td>
                <td>${transaction.amount.toLocaleString()}</td>
                <td>{transaction.merchant}</td>
                <td>{transaction.tenpistaName}</td>
                <td>{new Date(transaction.date[0], transaction.date[1] - 1, transaction.date[2]).toLocaleDateString()}</td>
                <td>
                  <FaEye className="icon-btn view-icon" onClick={() => handleEdit(transaction)} />
                  <FaTrash className="icon-btn delete-icon" onClick={() => handleDelete(transaction.id)} />
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="no-results">No hay transacciones disponibles.</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* 🔹 Paginación */}
      <div className="pagination">
        <button className="pagination-btn prev" onClick={() => goToPage(currentPage - 1)} disabled={currentPage === 0}>
          <FaChevronLeft />
        </button>
        <span className="pagination-text">Página {currentPage + 1} de {totalPages}</span>
        <button className="pagination-btn next" onClick={() => goToPage(currentPage + 1)} disabled={currentPage + 1 >= totalPages}>
          <FaChevronRight />
        </button>
      </div>

      {isCreateModalOpen && (
        <TransactionCreateModal token={token} onClose={handleCreateModalClose} onCreate={handleTransactionCreate} />
      )}

      {selectedTransaction && (
        <TransactionModal token={token} transaction={selectedTransaction} onClose={handleModalClose} onUpdate={handleTransactionUpdate} />
      )}
    </div>
  );
}

export default TransactionsTable;
