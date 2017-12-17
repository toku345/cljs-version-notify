# Standalone Usage

1. `lein figwheel`
2. (In another window) `node target\js\compiled\cljs_version_notify.js ...`


# Production Builds

1. `lein cljsbuild once prod`
2. `node index.js ...`


# Production Deploy

1. Build production code

2. Package

``` console
$ zip -r app.zip index.js node_modules/*

$ aws cloudformation package \
  --template-file sam-template.yml \
  --output-template-file sam-template-output.yml \
  --s3-bucket {bucket_name}
```

4. Deploy

``` console
$ aws cloudformation deploy \
  --template-file sam-template-output.yml \
  --stack-name {my-stack-name} \
  --capabilities CAPABILITY_IAM
```

Rewrite your own s3-bucket


# REPL Usage (Emacs)
Use cider!

# REPL Usage (Vim)

You can now connect to Figwheel's REPL through
[Piggieback](https://github.com/cemerick/piggieback) using
[vim-fireplace](https://github.com/tpope/vim-fireplace):

1. `lein repl`
2. `(fig-start)`
3. `(cljs-repl)`
4. (In another window) `node target\js\compiled\cljs_version_notify.js ...`
5. (In Vim) `:Piggieback (figwheel-sidecar.repl-api/repl-env)`

Standard `vim-fireplace` commands will now work in the context of the
Figwheel process:

- `cqp` to send a command from Vim to the REPL
- `cpa...` to evaluate a form without saving or reloading the file
- etc.
