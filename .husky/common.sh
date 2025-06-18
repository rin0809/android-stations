command_exists () {
  command -v "$1" >/dev/null 2>&1
}

# Git Bash on Windows workaround
if command_exists winpty && test -t 1; then
  exec < /dev/tty
fi
