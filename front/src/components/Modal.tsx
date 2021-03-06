import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { delStudy, studyInfo as studyInfoFunc, applicationStudy } from '../API/index';
import { Icon } from '../elements/index';
import { ItemsType } from './Items';
import { initalStudy } from '../views/MainView';
import CloseIcon from '@mui/icons-material/Close';

const ModalWrap = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 100;
`;

const StudyModal = styled.div`
  width: 500px;
  height: 500px;
`;

const ModalTop = styled.div`
  height: 200px;
  border-radius: 10px;
  background-color: #ffffff;
  position: relative;
  overflow: hidden;
  & > div {
    width: 90%;
    position: absolute;
    top: 30px;
    left: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  & > div > h2 {
    font-weight: bold;
  }

  & > .leader {
    position: absolute;
    bottom: 30px;
    left: 30px;
    display: flex;
    align-items: flex-end;
    font-size: 20px;
    font-weight: bold;
  }

  & > img {
    width: 100%;
    height: 100%;
  }
`;
const ModalBottom = styled.div`
  padding: 30px 40px;
  border-radius: 10px;
  background-color: lightgray;
`;

const MenuList = styled.ul`
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid black;
`;

const MemuItem = styled.li`
  display: flex;
  flex-direction: column;
  align-items: center;

  & > span:nth-child(2) {
    padding: 20px 10px 10px;
  }
`;

interface PropsType {
  studyId: number;
  modalStateChange: (open: boolean, study: ItemsType, del: boolean) => void;
}

function Modal({ studyId, modalStateChange }: PropsType) {
  const [studyInfo, setstudyInfo] = useState<ItemsType>(initalStudy);
  const [leader, setLeader] = useState<MemberType>({
    id: 0,
    member: {
      email: '',
      grade: 0,
      id: 0,
      nickname: '',
    },
    studyAuth: '',
    studyStatus: '',
  });

  useEffect(() => {
    const infoStudy = async (studyId: number) => {
      const {
        data: { data },
      } = await studyInfoFunc(studyId);
      setstudyInfo(data);
    };

    infoStudy(studyId);
  }, []);

  interface MemberType {
    id: number;
    member: {
      email: string;
      grade: number;
      id: number;
      nickname: string;
    };
    studyAuth: string;
    studyStatus: string;
  }

  useEffect(() => {
    const leader: MemberType = studyInfo.studyMembers.filter((leader: MemberType) => leader.studyAuth === 'LEADER')[0];
    setLeader(leader);
  });

  // ??????

  const sliceDate = (start: string, end: string) => {
    const startYear = start.slice(0, 4);
    const startMonth = start.slice(5, 7);
    const endYear = end.slice(0, 4);
    const endMonth = end.slice(5, 7);

    if (startYear === endYear) {
      if (startMonth === endMonth) {
        return `${start.slice(5)} ~ ${end.slice(8)}`;
      } else {
        return `${start.slice(5)} ~ ${end.slice(5)}`;
      }
    } else {
      return `${start} ~ ${end}`;
    }
  };

  const joinStudy = async () => {
    try {
      const {
        data: { data },
      } = await applicationStudy(studyId);

      console.log('data', data);

      alert('????????? ?????? ??????!');
    } catch (err: any) {
      const error = err.response.data;

      if (error) {
        alert(error.message);
      } else {
        alert('????????? ?????? ??? ?????? ??????');
      }
    }
  };

  return (
    <ModalWrap>
      <StudyModal>
        <ModalTop>
          <div>
            <h2 className="studyTitle">{studyInfo.title}</h2>
            <Icon onClick={() => modalStateChange(false, { ...initalStudy }, false)}>
              <CloseIcon />
            </Icon>
          </div>
          <div className="leader">
            <span>{leader?.member.nickname}</span>
            <button type="button" onClick={joinStudy}>
              ??????
            </button>
          </div>
          <img src={studyInfo.files[0].path} alt="????????? ??????" />
        </ModalTop>
        <ModalBottom>
          <MenuList>
            <MemuItem>
              <span>?????? ???</span>
              <span>
                {studyInfo.studyMembers.length} / {studyInfo.maxMember}
              </span>
            </MemuItem>
            <MemuItem>
              <span>??????</span>
              <span>{studyInfo.subject.name}</span>
            </MemuItem>
            <MemuItem>
              <span>??????</span>
              <span>{sliceDate(studyInfo.startDate, studyInfo.endDate)}</span>
            </MemuItem>
          </MenuList>
          <div>
            <span>{/* {study.} */}</span>
          </div>
        </ModalBottom>
      </StudyModal>
    </ModalWrap>
  );
}

export default Modal;
