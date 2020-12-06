var path = require('path');

module.exports = {
  entry: './index.jsx',
  cache: true,
  mode: 'development',
  output: {
    path: __dirname,
    filename: '../src/main/resources/static/bundle.js'
  },
  resolve: {
    extensions: ['.js', '.jsx']
  },
  module: {
    rules: [
      {
        test: path.join(__dirname, '.'),
        exclude: /(node_modules)/,
        use: [{
          loader: 'babel-loader',
          options: {
            presets: ["@babel/preset-env", "@babel/preset-react"]
          }
        }]
      }
    ]
  },
  devServer: {
    port: 4200,
    contentBase: path.join(__dirname, 'src/html_templates'),
    watchContentBase: true,
    hot: true
  },
};