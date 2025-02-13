export const validateUserData = (userData) => {
    const errors = [];
  
    if (!userData.username || userData.username.trim().length < 3) {
      errors.push("El nombre de usuario debe tener al menos 3 caracteres.");
    }
  
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!userData.email || !emailRegex.test(userData.email)) {
      errors.push("El correo electrónico no es válido.");
    }
  
    if (!userData.password || userData.password.length < 6) {
      errors.push("La contraseña debe tener al menos 6 caracteres.");
    }
  
    return errors;
  };
  