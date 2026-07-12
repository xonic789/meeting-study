import subprocess
import os
import sys

required_environment = ("ADMIN_MAIL_ID", "ADMIN_MAIL_PASSWORD")
missing = [name for name in required_environment if not os.environ.get(name)]
if missing:
    print("필수 환경변수가 없습니다: " + ", ".join(missing), file=sys.stderr)
    sys.exit(2)

# gradlew build
subprocess.check_call(["./gradlew", "clean", "build"])

# docker-compose up : detach mode(background)
subprocess.check_call(["docker-compose", "up", "-d"])
