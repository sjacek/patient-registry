const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { BaseHrefWebpackPlugin } = require('base-href-webpack-plugin');
const ExtWebpackPlugin = require('@sencha/ext-webpack-plugin')
const portfinder = require('portfinder')

const webpack = require("webpack")
const AngularCompilerPlugin = require('@ngtools/webpack').AngularCompilerPlugin
const FilterWarningsPlugin = require('webpack-filter-warnings-plugin')

module.exports = function (env) {
  function get(it, val) {if(env == undefined) {return val} else if(env[it] == undefined) {return val} else {return env[it]}}

   //******* */
   var framework     = get('framework',     'angular')
   var contextFolder = get('contextFolder', './src')
   var entryFile     = get('entryFile',     './main.ts')
   var outputFolder  = get('outputFolder',  'build')
   const rules = [
    {test: /\.css$/, loader: ['to-string-loader', "style-loader", "css-loader"]},
    {test: /\.(png|svg|jpg|jpeg|gif)$/, use: ['file-loader']},
    {test: /\.html$/,loader: "html-loader"},
    {test: /\.ts$/,  loader: '@ngtools/webpack'}
  ]
   const resolve = {
    extensions: ['.ts', '.js', '.html']
  }
  //******* */

   var toolkit       = get('toolkit',       'modern')
   var theme         = get('theme',         'theme-material')
   var packages      = get('packages',      ['treegrid'])
   var script        = get('script',        '')
   var emit          = get('emit',          'yes')
   var profile       = get('profile',       '')
   var environment   = get('environment',   'development')
   var treeshake     = get('treeshake',     'no')
   var browser       = get('browser',       'yes')
   var watch         = get('watch',         'yes')
   var verbose       = get('verbose',       'no')
   var basehref      = get('basehref',      '/')

  const isProd = environment === 'production'
  portfinder.basePort = (env && env.port) || 1962
  return portfinder.getPortPromise().then(port => {
    const plugins = [
      new HtmlWebpackPlugin({template: "index.html",hash: false,inject: "body"}),
      new BaseHrefWebpackPlugin({ baseHref: basehref }),
      new ExtWebpackPlugin({
        framework: framework,
        toolkit: toolkit,
        theme: theme,
        packages: packages,
        script: script,
        emit: emit,
        port: port,
        profile: profile,
        environment: environment,
        treeshake: treeshake,
        browser: browser,
        watch: watch,
        verbose: verbose,
        inject: 'yes',
        intellishake: 'no'
      }),

      new AngularCompilerPlugin({
        tsConfigPath: './tsconfig.json',
        entryModule: "src/app/app.module#AppModule",
        mainPath: "./src/main.ts",
        skipCodeGeneration: true
      }),
      // new webpack.ContextReplacementPlugin(
      //   /\@angular(\\|\/)core(\\|\/)fesm5/,
      //   path.resolve(__dirname, 'src'),{}
      // ),
      // new FilterWarningsPlugin({
      //   exclude: /System.import/
      // }),
      new webpack.HotModuleReplacementPlugin()
    ]
    return {
      mode: environment,
      devtool: (environment === 'development') ? 'inline-source-map' : false,
      context: path.join(__dirname, contextFolder),
      entry: {
        vendor:  './vendor.ts',
        polyfills: "./polyfills.ts",
        main: entryFile
      },
      output: {
        path: path.join(__dirname, outputFolder),
        filename: "[name].js"
      },
      plugins: plugins,
      module: {
        rules: rules
      },
      resolve: resolve,
      performance: { hints: false },
      stats: 'none',
      optimization: { noEmitOnErrors: true },
      node: false,
      devServer: {
        contentBase: outputFolder,
        historyApiFallback: true,
        host: '0.0.0.0',
        port: port,
        disableHostCheck: false,
        hot: !isProd,
        compress: !isProd,
        inline:!isProd,
        stats: 'none'
      }
    }
  })
}
