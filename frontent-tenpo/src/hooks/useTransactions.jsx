import { useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchTransactions } from "../service/transactionServiceGet";


export const useTransactions = (token, page, size) => {
  const authToken = token || localStorage.getItem("authToken");
  const queryClient = useQueryClient();

  return useQuery({
    queryKey: ["transactions", page, size],
    queryFn: async () => {
      try {
        const data = await fetchTransactions(authToken, page, size);

        // 🔥 Guardar en caché local
        localStorage.setItem("cachedTransactions", JSON.stringify(data));

        return data;
      } catch (error) {
        if (error.message.includes("Demasiadas solicitudes")) {
          console.warn("⚠️ Error 429. Recuperando datos de caché local...");

          // 🔥 Recuperar del caché local si existe
          const cachedData = localStorage.getItem("cachedTransactions");
          if (cachedData) {
            return JSON.parse(cachedData);
          }

          // Si no hay caché, devolver un array vacío
          return { transactions: [], totalPages: 1, totalElements: 0 };
        }
        throw error;
      }
    },
    staleTime: 5 * 60 * 1000, // Cachea los datos por 5 minutos
    cacheTime: 10 * 60 * 1000, // Mantiene en caché por 10 minutos
    retry: false, // No reintentar para evitar más 429
    refetchOnWindowFocus: false, // No refetch al cambiar de pestaña
    keepPreviousData: true, // Mantiene los datos previos mientras carga nuevos
  });
};




  