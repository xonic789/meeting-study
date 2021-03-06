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
import { AxiosResponse } from 'axios';
import { deleteMessage, listMessage, message, readMessage } from '../ToolKit/messages';
import { ResSendMessage } from '../ToolKit/axiosType';

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
  callStudyList?: () => void;
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

function StudyColumnList({ items, index, callStudyList }: PropsType) {

interface ItemType {
  content: string;
  createdDate: string;
  id: number;
  lastUpdateDate: string;
  member: string;
  email: string;
  grade: number;
  nickname: string;
  sender: string;
  status: string;
}
  const Dispatch = useDispatch();
  const [messages, setMessages] = useState();
  const [memberList, setMemberList] = useState<MemberType[]>([]);
  const messageData: PayloadProps = useSelector(message); // ????????? ??????
  const userInfo = useSelector(userStatus);

  console.log("messageData",messageData);

  const [listMessage, setListMessage] = useState(messageData?.payload?.payload?.data);
  // console.log('List:items', items);
  // console.log('List:index', index);
  console.log('messageData', messageData);
  const itemRef = useRef<HTMLLIElement>(null);
  const contentMaxRef = useRef<HTMLDivElement>(null);
  const contentSliceRef = useRef<HTMLDivElement>(null);
  // 2. onClick ???????????? ????????????
  const onClick = async (item: ItemType, e: any) => {
    // setMessages(item); // data
    const parent = e.currentTarget.childNodes[0].childNodes[1];
    const contentSlice = parent.childNodes[2];
    const contentMax = parent.childNodes[3];

    // console.log('item', item.);

    contentSlice.classList.toggle('open');
    contentMax.classList.toggle('open');
    // console.log('item', item);
    Dispatch(readMessage(item.id));
    // console.log('messagessssz', Read);
    // Dispatch(readMessage(data.id));
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

      if (window.confirm('?????? ?????????????????????????')) {
        const leader = foundLeader(study.studyMembers);

        const res = await delStudy(leader.member.id, leader.member.nickname, study.id);
        if (res.status === 204) {
          alert('?????? ??????!');
          if (callStudyList) {
            callStudyList();
          }
        }
      }
    } catch (err: any) {
      console.log('err', err);
      const error = err.response.data;

      if (error) {
        alert(error.message);
      } else {
        alert('?????? ?????? ??????');
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
                    <span>?????? ?????? ?????? : {item.maxMember}</span>
                  </div>
                  <div>
                    <span>?????? : {item.subject.name}</span>
                  </div>
                </div>
                <StudyInfo className="study-info">
                  {foundLeader(item.studyMembers).member.id === userInfo.id && (
                    <>
                      <div className="study-info-top">
                        <Link to={`/study/modify?studyId=${item.id}`} className="modify-study">
                          ??????
                        </Link>
                        <button className="delete-study" onClick={(e) => deleteStudy(e, item)}>
                          ??????
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
                  onClick={(e) => onClick(item, e)}
                >
                  <div style={{ display: 'flex' }}>
                    <div style={{ paddingRight: '20px' }}>
                      <img src={person} alt="??????" />
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
