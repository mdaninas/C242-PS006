const axios = require('axios');
const NodeCache = require('node-cache');
const newsCache = new NodeCache({ stdTTL: 600 });

const fetchHealthNews = async () => {
  if (newsCache.has('healthNews')) return newsCache.get('healthNews');

  const response = await axios.get('https://api-berita-indonesia.vercel.app/cnn/gayaHidup/');
  const articles = response.data.data.posts; //pastikan sesuai dengan api
  newsCache.set('healthNews', articles);
  return articles;
};

module.exports = { fetchHealthNews };
