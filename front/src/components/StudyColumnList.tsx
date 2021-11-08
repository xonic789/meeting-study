import React, { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';
import { Icon } from '../elements';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import person from '../asset/image/person.png';
import moment from 'moment';
import { deleteMessage, listMessage, message } from '../ToolKit/messages';

interface Study {
  createdDate: string;
  dtype: string;
  endDate: string;
  files: {
    id: number;
    name: string;
    path: string;
  }[];
  id: number;
  lastUpdateDate: string;
  maxMember: number;
  offline: boolean;
  online: {
    id: number;
    link: string;
    onlineType: string;
  };
  startDate: string;
  studyMembers: {
    id: number;
    member: {
      email: string;
      grade: number;
      id: number;
      nickname: string;
    };
    studyAuth: string;
    studyMemberStatus: string;
  }[];
  studyType: string;
  subject: {
    id: number;
    name: string;
  };
  title: string;
}
interface PropsType {
  items?: any;
  index: number;
}

const StudyItem = styled.li`
  & > .study-top > span {
    transition: 0.2s linear;
    transform: rotate(0deg);
  }
  &.open {
    & > .study-top > span {
      transition: 0.2s linear;
      transform: rotate(90deg);
    }
    & > ul {
      transition: 0.2s linear;
      display: block;
    }
  }
`;

const StudyMemberList = styled.ul`
  display: none;
`;

const Contents = styled.div`
  max-width: 390px;
  height: max-content;
  word-break: break-all;
  display: none;

  &.open {
    display: block;
  }
`;
interface PayloadProps {
  payload: {
    payload: {
      data: any;
    };
  };
}
function StudyColumnList({ items, index }: PropsType) {
  const Dispatch = useDispatch();
  const [messages, setMessages] = useState();
  const messageData: PayloadProps = useSelector(message); // 리덕스 변수

  const [listMessage, setListMessage] = useState(messageData?.payload?.payload?.data);
  // console.log('List:items', items);
  // console.log('List:index', index);
  console.log('messageData', messageData);
  const itemRef = useRef<HTMLLIElement>(null);
  const contentMaxRef = useRef<HTMLDivElement>(null);
  const contentSliceRef = useRef<HTMLDivElement>(null);
  // 2. onClick 함수에서 처리하기
  const onClick = (data: any, idx: number, e: any) => {
    setMessages(data); // data
    const parent = e.currentTarget.childNodes[0].childNodes[1];
    const contentSlice = parent.childNodes[2];
    const contentMax = parent.childNodes[3];

    contentSlice.classList.toggle('open');
    contentMax.classList.toggle('open');
  };
  const onDelete = (e: any, item: any) => {
    console.debug('kk', item);
    Dispatch(deleteMessage(item.id));
  };
  useEffect(() => {
    setListMessage(messageData?.payload?.payload?.data);
  }, [onDelete]);
  console.log(listMessage, 'listMessage12312');

  const clickStudy = (e: React.MouseEvent<HTMLLIElement>) => {
    e.currentTarget.classList.toggle('open');
  };

  // JSX
  return (
    <div style={{ width: '500px', justifyContent: 'flex-start' }}>
      {/* <h3>총 {items.length}개</h3> */}
      <hr />
      <ul style={{ padding: '0px 10px' }}>
        {index === 1 &&
          items.map((item: Study, idx: number) => {
            return (
              <StudyItem key={idx} style={{ margin: '20px 0px', cursor: 'pointer' }} onClick={clickStudy}>
                <div
                  className="study-top"
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}
                >
                  <h2>
                    {item.title}{' '}
                    <span
                      style={{
                        fontSize: '18px',
                        fontWeight: 'lighter',
                      }}
                    >
                      - {item.dtype}
                    </span>
                  </h2>
                  <Icon>
                    <ArrowForwardIosIcon />
                  </Icon>
                </div>
                <div className="item-bottom" style={{ marginTop: '10px' }}>
                  <div>
                    <span>최대 참여 인원 : {item.maxMember}</span>
                  </div>
                  <div>
                    <span>언어 : {item.subject.name}</span>
                  </div>
                </div>
                <StudyMemberList>
                  {item.studyMembers.map((member) => {
                    return <li key={member.member.id}>{member.member.nickname}</li>;
                  })}
                </StudyMemberList>
              </StudyItem>
            );
          })}
        {index === 2 &&
          listMessage &&
          listMessage.map((item: any, idx: number) => {
            const date = moment(item.createdDate).format('YYYY-MM-DD HH:mm:ss');
            const contents = item.content.substring(0, 40);
            const contentsMax = item.content;
            return (
              <>
                <li
                  key={idx}
                  ref={itemRef}
                  style={{ margin: '20px 0px', cursor: 'pointer' }}
                  onClick={(e) => onClick(item, idx, e)}
                >
                  <div style={{ display: 'flex' }}>
                    <div style={{ paddingRight: '20px' }}>
                      <img src={person} alt="유저" />
                    </div>
                    <div>
                      <span>{date}</span>
                      <button onClick={(e) => onDelete(e, item)}>Del</button>
                      <Contents ref={contentSliceRef} className="open">
                        {contents}
                      </Contents>
                      <Contents ref={contentMaxRef}>{contentsMax}</Contents>
                    </div>
                  </div>
                </li>
              </>
            );
          })}
      </ul>
    </div>
  );
}

export default StudyColumnList;
