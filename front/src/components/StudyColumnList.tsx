import React, { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';
import { delStudy, member, studyInfo as studyInfoFunc, studyMemberList } from '../API/index';
import { Icon } from '../elements';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import person from '../asset/image/person.png';
import moment from 'moment';
import { ItemsType } from './Items';
import { Link } from 'react-router-dom';
import { userStatus } from '../ToolKit/user';
import { deleteMessage, listMessage, message } from '../ToolKit/messages';
import { AxiosResponse } from 'axios';

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

const initialStudy = {
  createdDate: '',
  dtype: '',
  endDate: '',
  files: [
    {
      id: 0,
      name: '',
      path: '',
    },
  ],
  id: 0,
  lastUpdateDate: '',
  maxMember: 0,
  offline: {
    id: 0,
    address: {
      address1: '',
      address2: '',
      address3: '',
      id: 0,
    },
  },
  online: {
    id: 0,
    link: '',
    onlineType: '',
  },
  startDate: '',
  studyMembers: [
    {
      id: 0,
      member: {
        email: '',
        grade: 0,
        id: 0,
        nickname: '',
      },
      studyAuth: '',
      studyStatus: '',
    },
  ],
  studyType: '',
  subject: {
    id: 0,
    name: '',
  },
  title: '',
  content: '',
};

const initalLeader = {
  id: 0,
  member: {
    email: '',
    grade: 0,
    id: 0,
    nickname: '',
  },
  studyAuth: '',
  studyStatus: '',
};

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
    & > .study-info {
      transition: 0.2s linear;
      display: block;
    }
    & > .study-info > .study-info-top {
      display: flex;
    }
    & > .study-info > .study-info-top > * {
      width: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 5px;
      color: #fff;
      cursor: pointer;
    }
    & > .study-info > .study-info-top > .modify-study {
      background-color: #1648ec;
    }
    & > .study-info > .study-info-top > .delete-study {
      background-color: #ec0000;
    }
  }
`;

const StudyInfo = styled.div`
  display: none;
  margin-top: 10px;
`;

const StudyMemberList = styled.ul`
  margin-top: 10px;
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
  const [memberList, setMemberList] = useState<MemberType[]>([]);
  const messageData: PayloadProps = useSelector(message); // 리덕스 변수
  const userInfo = useSelector(userStatus);

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

  const clickStudy = async (e: React.MouseEvent<HTMLLIElement>, studyId: number) => {
    e.stopPropagation();
    e.currentTarget.parentNode?.childNodes.forEach((item) => {
      if (item !== e.currentTarget) {
        // @ts-ignore
        item.classList.remove('open');
      }
    });
    e.currentTarget.classList.toggle('open');

    await listMember(studyId);
  };

  const checkNull = (obj: object) => {
    if (obj === null) {
      return { null: null };
    }
    return obj;
  };

  const makeObjectQueryString = (obj: any | null) => {
    let url = '';

    const checkObj = checkNull(obj as object);

    for (let prop in checkObj) {
      // @ts-ignore
      url += `${prop}=${checkObj[prop]}&`;
    }

    return url;
  };

  const makeQueryString = (study: Study) => {
    let url = '/study/modify/type=modify&';
    for (let prop in study) {
      if (prop === 'studyMembers') {
        continue;
      }
      // @ts-ignore
      url = url += `${prop}=${
        // @ts-ignore
        typeof study[prop] === 'object'
          ? // @ts-ignore
            encodeURIComponent(makeObjectQueryString(prop === 'files' ? study[prop][0] : study[prop]))
          : // @ts-ignore
            encodeURIComponent(study[prop])
      }&`;
    }

    // 마지막 & 제거
    url = url.substr(0, url.length - 1);

    return url;
  };

  interface MemberType {
    id: number;
    member: {
      email: string;
      grade: number;
      id: number;
      nickname: string;
    };
    studyAuth: string;
    studyMemberStatus: string;
  }

  const deleteStudy = async (e: React.MouseEvent<HTMLButtonElement>, study: Study) => {
    try {
      e.stopPropagation();

      if (window.confirm('정말 삭제하시겠습니까?')) {
        const leader = foundLeader(study.studyMembers);

        const res = await delStudy(leader.member.id, leader.member.nickname, study.id);
        if (res.status === 204) {
          alert('삭제 성공!');
        }
      }
    } catch (err: any) {
      console.log('err', err);
      const error = err.response.data;

      if (error) {
        alert(error.message);
      } else {
        alert('서버 에러 발생');
      }
    }
  };

  const foundLeader = (members: MemberType[]) => {
    const leader: MemberType = members.filter((member: MemberType) => member.studyAuth === 'LEADER')[0];

    const MockleaderObj: MemberType = {
      id: 0,
      member: {
        email: '',
        grade: 0,
        id: 0,
        nickname: '',
      },
      studyAuth: '',
      studyMemberStatus: '',
    };

    if (leader === undefined) {
      return MockleaderObj;
    }

    return leader;
  };

  const listMember = async (studyId: number) => {
    interface MemberListType {
      data: MemberType[];
    }

    try {
      const {
        data: { data },
      }: AxiosResponse<MemberListType> = await studyMemberList(studyId);

      setMemberList(data);
    } catch (err) {
      console.log('err', err);
    }
  };

  useEffect(() => {
    return () => setMemberList([]);
  }, []);

  // JSX
  return (
    <div style={{ width: '500px', justifyContent: 'flex-start' }}>
      {index === 1 && <h3>총 {items.length}개</h3>}
      <hr />
      <ul style={{ padding: '0px 10px' }}>
        {index === 1 &&
          items.map((item: Study, idx: number) => {
            return (
              <StudyItem
                key={idx}
                style={{ margin: '20px 0px', cursor: 'pointer' }}
                onClick={(e) => clickStudy(e, item.id)}
              >
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
                <StudyInfo className="study-info">
                  {foundLeader(item.studyMembers).member.id === userInfo.id && (
                    <>
                      <div className="study-info-top">
                        <Link to={() => makeQueryString(item)} className="modify-study">
                          수정
                        </Link>
                        <button className="delete-study" onClick={(e) => deleteStudy(e, item)}>
                          삭제
                        </button>
                      </div>
                    </>
                  )}

                  <div className="member-list">
                    <StudyMemberList>
                      {memberList.map((member: MemberType) => {
                        return (
                          <li key={member.member.id}>
                            <Link to={`/my/message/send?email=${member.member.email}`}>{member.member.nickname}</Link>
                          </li>
                        );
                      })}
                    </StudyMemberList>
                  </div>
                </StudyInfo>
              </StudyItem>
            );
          })}
        {index === 2 &&
          listMessage &&
          listMessage.map((item: any, idx: number) => {
            const date = moment(item.createdDate).format('YYYY-MM-DD HH:mm:ss');
            const contents = item.content.substring(0, 40);
            const contentsMax = item.content;
            const nickName = item.sender.nickname;
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
                      <span>{nickName}</span> <span>{date}</span>
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
