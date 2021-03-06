import React, { useState, useEffect } from 'react';
import styled, { css } from 'styled-components';
import { deleteUser, getMyStudy } from '../API/index';
import StudyHeader from '../components/StudyHeader';
import StudyColumnList from '../components/StudyColumnList';
import { Main, Section, InputWrap, Input, InputTitle, Button, Icon } from '../elements';
import PermIdentityIcon from '@material-ui/icons/PermIdentity';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import MailOutlineIcon from '@material-ui/icons/MailOutline';
import person from '../asset/image/person.png';
import { Link, useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { listMessage, message } from '../ToolKit/messages';
import StudyFooter from '../components/StudyFooter';

const SelectItem = styled.div`
  width: 500px;
  background-color: #f8f8f8;
`;

const Items = styled.ul`
  display: flex;
  flex-wrap: wrap;
  padding: 20px;
`;

const Item = styled.li`
  width: 110px;
  padding: 20px;
  cursor: pointer;

  &.selected {
    color: #8dc6fb;
  }
`;

export const items = [
  {
    title: '개인정보',
    image: <PermIdentityIcon />,
    url: '/my/info',
    name: 'info',
    component: Info,
  },
  {
    title: '스터디',
    image: <MenuBookIcon />,
    url: '/my/study',
    name: 'study',
    component: Study,
  },
  {
    title: '쪽지',
    image: <MailOutlineIcon />,
    url: '/my/message',
    name: 'message',
    component: Message,
  },
  {
    title: '회원탈퇴',
    image: <PermIdentityIcon />,
    url: '/my/secession',
    name: 'secession',
    component: Secession,
  },
];

interface PayloadProps {
  payload: {
    payload: {
      data: any;
    };
  };
}
function MyStudyView() {
  const Dispatch = useDispatch();

  const history = useHistory();
  const [study, setStudy] = useState<any[]>([]);
  const [inputs, setInputs] = useState({
    email: '',
    nickname: '',
    password: '',
  });
  const [item, setItem] = useState(0); // 이코드로 스터디 쪽지 구분함

  console.log('item', item);
  const onClick = (e: any, index: number) => {
    // 나중에 코드 수정해야 함
    const lis = document.querySelectorAll('li');
    lis.forEach((target) => {
      if (target === e) {
        target.classList.add('selected');
      } else {
        target.classList.remove('selected');
      }
    });
    setItem(index);

    // 이동 시 초기화
    setInputs({
      email: '',
      nickname: '',
      password: '',
    });
  };

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setInputs({
      ...inputs,
      [name]: value,
    });
  };
  const obj: any = {
    info: '0',
    study: '1',
    message: '2',
    secession: '3',
  };

  const pathname = history.location.pathname;
  // const pathnameSlice = pathname.slice(4);
  // setItem(parseInt(obj[pathnameSlice]));
  useEffect(() => {
    const pathnameSlice = pathname.slice(4);
    setItem(parseInt(obj[pathnameSlice]));
    Dispatch(listMessage());
  }, []);

  return (
    <>
      <StudyHeader>MY 스터디</StudyHeader>
      <Main>
        <Section>
          <SelectItem>
            <Items>
              {items.map(({ image, title, url, name }: any, index: number) => {
                const obj: any = {
                  info: '0',
                  study: '1',
                  message: '2',
                  secession: '3',
                };
                const pathname = history.location.pathname;
                const pathnameSlice = pathname.slice(4);
                // console.log(typeof pathname);
                // console.log(pathnameSlice);
                return (
                  <React.Fragment key={index}>
                    <Item
                      onClick={(e) => onClick(e.currentTarget, index)}
                      key={index}
                      className={index === parseInt(obj[pathnameSlice]) ? 'selected' : ''}
                    >
                      <Link to={url}>
                        <Icon style={{ display: 'flex', flexDirection: 'column' }}>
                          {image}
                          <span>{title}</span>
                        </Icon>
                      </Link>
                    </Item>
                  </React.Fragment>
                );
              })}
            </Items>
          </SelectItem>
          <div style={{ marginTop: '50px' }}>
            {pathname === '/my/info' && <Info onChange={onChange} />}
            {pathname === '/my/study' && <Study index={item} />}
            {pathname === '/my/message' && <Message index={item} />}
            {pathname === '/my/secession' && <Secession onChange={onChange} />}
          </div>
        </Section>
      </Main>
      <StudyFooter />
    </>
  );
}

export default MyStudyView;

interface PropsType {
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}
//ts-ignore
export function Info({ onChange }: PropsType) {
  // console.log('props', props);
  return (
    <>
      <div style={{ width: '500px', display: 'flex' }}>
        <div style={{ padding: '10px 20px' }}>
          <img src={person} alt="유저" />
        </div>
        <div>
          <InputWrap>
            <InputTitle htmlFor="nickname">닉네임</InputTitle>
            <Input id="nickname" name="nickname" type="text" onChange={onChange} placeholder="닉네임" />
          </InputWrap>
          <Button style={{ backgroundColor: '#51aafe' }}>수정하기</Button>
        </div>
      </div>
    </>
  );
}
interface PType {
  study?: any;
  post?: any;
  index: any;
}

export function Study({ index }: PType) {
  const [study, setStudy] = useState([]);

  const callMyStudy = async () => {
    try {
      const {
        data: { data },
      } = await getMyStudy();

      setStudy(data);
    } catch (err) {
      console.log('스터디 불러오기 실패');
    }
  };
  useEffect(() => {
    callMyStudy();
  }, []);

  const callStudyList = () => {
    callMyStudy();
  };

  return (
    <>
      <StudyColumnList items={study} index={index} callStudyList={callStudyList} />
    </>
  );
}

export function Message({ index }: PType) {
  // console.log('post111111', post);
  return (
    <>
      <StudyColumnList index={index} />
    </>
  );
}
export function Secession({ onChange }: PropsType) {
  const history = useHistory();
  const userDelete = async () => {
    try {
      await deleteUser();
      alert('회원탈퇴 성공!');
      history.push('/');
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      <div>
        <h3 style={{ marginBottom: '30px', fontWeight: 'lighter' }}>정말 탈퇴하시겠습니까?</h3>
        <InputWrap>
          <InputTitle htmlFor="password">비밀번호</InputTitle>
          <Input id="password" name="password" type="password" onChange={onChange} placeholder="비밀번호" />
        </InputWrap>
        <Button style={{ backgroundColor: '#51aafe' }} onClick={userDelete}>
          탈퇴
        </Button>
      </div>
    </>
  );
}
