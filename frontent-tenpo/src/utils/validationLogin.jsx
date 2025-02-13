export const validateLoginData = (userData) => {
    const errors = [];
  
    if (!userData.username || userData.username.trim().length < 3) {
      errors.push("El nombre de usuario debe tener al menos 3 caracteres.");
    }
  
    if (!userData.password || userData.password.length < 6) {
      errors.push("La contraseña debe tener al menos 6 caracteres.");
    }
  
    return errors;
  };
  