#!/bin/sh

if [ -d sbt-verify ]; then
  cd sbt-verify
  git pull
else
  git clone https://github.com/input-output-hk/sbt-verify
  cd sbt-verify
fi
nix-shell -p sbt --run "sbt publishLocal"
