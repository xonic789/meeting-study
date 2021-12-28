import subprocess
import base64
import sys


def addNewline(strings):
    if '\n' not in strings:
        return strings + '\n'
    return strings

def getEncodedStr(strings):
    strBytes = strings.encode('ascii')
    return base64.b64encode(strBytes).decode('ascii')    

def getUserInfo():
    email = input("이메일을 입력해주세요 : ")
    password = input("패스워드를 입력해주세요 : ")
    # bytes 타입 변환 및 암호화
    email = getEncodedStr(email)
    password = getEncodedStr(password) 
    return email, password
    
FILEPATH = "./src/main/resources/email.properties"
f = open(FILEPATH)


#TODO
#   1. 딕셔너리 자료구조에 해당 파일 넣기
#   2. 딕셔너리 자료구조에 AdminMail.id, AdminMail.password없다면 입력 대기상태로 만들어준다.
#   3. 있다면 변경할거냐고 물어본다.
print("프로그램의 이메일 인증 메일을 전송할 어드민 계정을 입력해야합니다\n")
print("절대 이메일이나 패스워드가 외부에 공개될 일은 없습니다")
print("입력하지 않으면 WAS는 종료되며 정확히 입력하지 않으면 회원가입 인증 메일이 전송되지 않습니다")
print("메일 전송 기능은 STMP를 기반으로 작성하였으며, gmail로 테스트 하였습니다. 인증 단계를 1단계로 변경하여야 이메일 전송이 가능합니다")
print("아이디와 비밀번호는 암호화되어 저장됩니다")

EMAIL = 'AdminMail.id'
PASSWORD = 'AdminMail.password'
fileDic = {}
while True:
    line = f.readline() 
    if not line: break
    if '=' not in line or line == '\n' or '#' in line:
        continue
    tmp = line.split('=')
    fileDic[tmp[0]] = tmp[1]
f.close()
#f = open(FILEPATH, w)
# 이메일과 패스워드가 키로 존재하지 않는다면,
# 입력 받아야한다
# 키로 있다면 변경하겠냐는 메시지를 준다.
email = ''
password = ''
if EMAIL in fileDic and PASSWORD in fileDic:
    userInput = input("이메일과 패스워드가 이미 존재합니다. 이메일과 패스워드를 입력하시겠습니까?(y or n) ")
    if userInput == 'y':
        email, password = getUserInfo()
else:
    userInput = input("이메일과 패스워드를 입력하시겠습니까?(y or n) ")
    if userInput != 'y':
        print("이메일과 패스워드가 입력되지 않았습니다. 프로그램이 종료됩니다")
        quit()
    email, password = getUserInfo()


# 이메일과 패스워드가 채워져있으면 변경해야하므로 다시 초기화해준다.
if email and password:
    fileDic[EMAIL] = email
    fileDic[PASSWORD] = password

# 만약 키가 존재하지 않으면 바로 종료
if not fileDic[EMAIL] and fileDic[PASSWORD]:
    print("이메일과 패스워드가 비었습니다. 프로그램이 종료됩니다")
    quit()

fileDic[EMAIL] = addNewline(fileDic[EMAIL])
fileDic[PASSWORD] = addNewline(fileDic[PASSWORD])
f = open(FILEPATH, 'w')

for k in fileDic.keys():
    f.write(k + "=" + fileDic[k])
f.close()
# gradlew build
subprocess.call(["./gradlew", "clean", "build"])

# docker-compose up : detach mode(background)
subprocess.call(["docker-compose", "up", "-d"])

#temp = subprocess.call(["ls", "-al"])
