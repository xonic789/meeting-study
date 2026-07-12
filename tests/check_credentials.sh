#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

command -v rg >/dev/null || { printf 'ripgrep is required\n' >&2; exit 1; }

if rg -n --hidden --glob '!.git/**' --glob '!**/tests/check_credentials.sh' \
  '(AKIA[0-9A-Z]{16}|ASIA[0-9A-Z]{16}|AIza[0-9A-Za-z_-]{35}|github_pat_|ghp_|BEGIN (RSA |EC |OPENSSH )?PRIVATE KEY)' \
  "$ROOT"; then
  printf 'Credential-like value found\n' >&2
  exit 1
fi

rg -q '@Value\("\$\{ADMIN_MAIL_ID\}"\)' "$ROOT/src/main/java/study/devmeetingstudy/config/EmailConfig.java"
rg -q '@Value\("\$\{ADMIN_MAIL_PASSWORD\}"\)' "$ROOT/src/main/java/study/devmeetingstudy/config/EmailConfig.java"

printf 'Credential safety checks passed\n'
