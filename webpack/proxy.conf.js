function setupProxy({ tls }) {
  const conf = [
    {
      context: [
        '/api',
        '/services',
        '/management',
        '/v3/api-docs',
        '/h2-console',
        '/auth',
        '/health',
        '/login/oauth2/code/google',
        '/oauth2/authorization/google',
        '/login/oauth2/code/facebook',
        '/oauth2/authorization/facebook',
        '/v2.8/oauth/access_token',
      ],
      target: `http${tls ? 's' : ''}://localhost:8080`,
      secure: false,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy;
