import React, { useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import Sidebar from "../components/Sidebar";

import Home from "../components/Home";

import Transactions from "./Transactions";
import "../styles/dashboard.css"; // 🔥 Importamos los estilos

function Dashboard() {
  const queryClient = useQueryClient();
  const [isSidebarOpen, setIsSidebarOpen] = useState(true); // Estado para el sidebar

  const handleLogout = () => {
    localStorage.removeItem("authToken");
    queryClient.removeQueries(["authToken"]);
  };

  return (
    <div className={`dashboard-container ${isSidebarOpen ? "expanded" : "collapsed"}`}>
      <Sidebar handleLogout={handleLogout} setIsSidebarOpen={setIsSidebarOpen} />
      <div className="dashboard-content">
        <Routes>
          <Route path="/" element={<Navigate to="home" />} />
          <Route path="home" element={<Home />} />
          <Route path="transactions" element={<Transactions />} />
        </Routes>
      </div>
    </div>
  );
}

export default Dashboard;
