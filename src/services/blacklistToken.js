const blacklist = new Set(); 

const addTokenToBlacklist = (token) => {
  blacklist.add(token); 
};

const isTokenBlacklisted = (token) => {
  return blacklist.has(token); 
};

module.exports = { addTokenToBlacklist, isTokenBlacklisted };
