import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  root: ".", // 🔹 Mantiene la raíz actual
  publicDir: "public", // 🔹 Evita conflictos con public/
  build: {
    outDir: "dist", // 🔹 Define la carpeta de salida
    rollupOptions: {
      input: "index.html", // 🔹 Fuerza a Vite a usar `index.html` en la raíz
    },
  },
  server: {
    port: 5173,
    host: "0.0.0.0", // 🟢 Permitir acceso desde Docker
  }
});
