import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import App from "./App";
import "./index.css";

// 🔥 Configurar el cliente de React Query con manejo de caché
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60000, // 🔥 Cache válido por 1 minuto
      cacheTime: 300000, // 🔥 Guardar en caché por 5 minutos
      refetchOnWindowFocus: false, // ❌ Evita que se vuelva a consultar automáticamente
      retry: false, // ❌ No reintentar solicitudes fallidas
    },
  },
});

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </React.StrictMode>
);
