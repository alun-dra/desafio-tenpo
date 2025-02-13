import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom"; // Importamos useNavigate
import { FaBars, FaTimes, FaHome, FaExchangeAlt, FaSignOutAlt } from "react-icons/fa";
import "../styles/sidebar.css"; // Asegurar que los estilos están cargados

function Sidebar() {
  const [isOpen, setIsOpen] = useState(window.innerWidth > 768); // Sidebar abierto en pantallas grandes
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768);
  const navigate = useNavigate(); // Hook para redirigir

  // Detectar cambios en el tamaño de la pantalla
  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
      if (window.innerWidth > 768) {
        setIsOpen(true); // Mantener abierto en pantallas grandes
      } else {
        setIsOpen(false); // Ocultar por defecto en móviles
      }
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // 🔹 Función para cerrar sesión correctamente
  const handleLogout = () => {
    localStorage.removeItem("authToken"); // 🔥 Eliminar el token del almacenamiento local
    navigate("/"); // 🔥 Redirigir al usuario a la página de inicio
  };

  return (
    <>
      {/* 🔹 Botón hamburguesa flotante en móviles */}
      <button className="hamburger-float" onClick={() => setIsOpen(!isOpen)}>
        {isOpen ? <FaTimes /> : <FaBars />}
      </button>

      {/* 🔹 Sidebar principal */}
      <div className={`sidebar ${isOpen ? "open" : "closed"} ${isMobile ? "mobile" : ""}`}>
        <div className="sidebar-header">
          {!isMobile && (
            <button className="hamburger" onClick={() => setIsOpen(!isOpen)}>
              {isOpen ? <FaTimes /> : <FaBars />}
            </button>
          )}
        </div>

        {/* 🔹 Menú de Navegación */}
        {isOpen && (
          <nav>
            <ul>
              <li>
                <Link to="/dashboard/home">
                  <FaHome /> Home
                </Link>
              </li>
              <li>
                <Link to="/dashboard/transactions">
                  <FaExchangeAlt /> Transacciones
                </Link>
              </li>
            </ul>
          </nav>
        )}

        {/* 🔹 Título "Tenpo" y Cerrar Sesión en la parte inferior */}
        {isOpen && (
          <div className="sidebar-footer">
            <div className="sidebar-title">Desafio Tenpo</div>
            <button className="logout" onClick={handleLogout}>
              <FaSignOutAlt /> Cerrar Sesión
            </button>
          </div>
        )}
      </div>
    </>
  );
}

export default Sidebar;
