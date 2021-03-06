import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { getSubjects } from '../API/index';
import { Main, Section, Icon } from '../elements';
import StudyHeader from '../components/StudyHeaderRemove';
import StudyFooter from '../components/StudyFooter';
import Items from '../components/Items';
import StudyModal from '../components/Modal';
import { ItemsType } from '../components/Items';

const Select = styled.select`
  height: 40px;
  border: none;
  outline: none;
  padding: 10px;
  border-radius: 5px;
  margin-right: 30px;
  background-color: #f8f8f8;
  cursor: pointer;
`;

const ItemList = styled.ul`
  display: inline-flex;
  align-content: flex-start;
  flex-wrap: wrap;
  margin-top: 30px;
  @media screen and (max-width: 740px) {
    width: 90%;
    flex-wrap: no-wrap;
  }
`;
export const Item = styled.li`
  width: 360px;
  height: 200px;
  position: relative;
  border: 1px solid #000;
  border-radius: 8px;
  margin-bottom: 20px;
  cursor: pointer;
  overflow: hidden;

  & > img {
    width: 100%;
    height: 100%;
  }

  &:nth-child(odd) {
    margin-right: 16px;
  }
  @media screen and (max-width: 740px) {
    & {
      width: 100%;
      margin-right: 0px;
    }
    &:nth-child(odd) {
      margin-right: 0px;
    }
  }
`;
const SidoGungu = styled(Select)`
  @media screen and (max-width: 740px) {
    transform: translateX(35px);
    margin-top: 10px;
    white-space: pre-wrap;
  }
`;
// 초기 값
export const initalStudy = {
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

function OpenStudyView() {
  const [subjects, setSubjects] = useState([]);
  const [modal, setModal] = useState({
    open: false,
    study: { ...initalStudy } as ItemsType,
    del: false,
  });
  const [inputs, setInputs] = useState({
    dtype: 'ONLINE',
    address1: null,
    lastId: null,
    studyType: 'FREE',
    sorted: 'DESC',
    subjectId: 0,
    title: null,
    offset: 4,
  });

  const onChange = (e: React.ChangeEvent<HTMLSelectElement> | React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (name === 'subjectId') {
      const options = Array.from(e.target.children);

      const idx = options.filter((item: Element) => {
        if (item.innerHTML === value) {
          return item;
        }
      });

      setInputs({
        ...inputs,
        subjectId: value === 'ALL' ? parseInt('0') : parseInt(idx[0].id),
      });
    } else {
      setInputs({
        ...inputs,
        [name]: name === 'offset' ? parseInt(value) : value,
      });
    }
  };

  const modalStateChange = (open: boolean, study: ItemsType, del: boolean) => {
    setModal({
      open,
      study,
      del,
    });
  };

  useEffect(() => {
    const getSubject = async () => {
      const {
        data: { data },
      } = await getSubjects();
      setSubjects(data);
    };
    getSubject();
  }, []);

  useEffect(() => {
    if (modal.open) {
      document.body.style.overflow = 'hidden';
    } else {
      //@ts-ignore
      document.body.style.overflow = 'initial';
    }
  }, [modalStateChange]);

  return (
    <>
      <StudyHeader />
      {/* ype 'object | ItemsType' is not assignable to type 'ItemsType'.
  Type '{}' is missing the following properties from type 'ItemsType': createdDate, dtype, endDate, files, and 8 more.ts(2322 */}
      {modal.open && <StudyModal studyId={modal.study.id} modalStateChange={modalStateChange} />}
      <Main>
        <Section>
          <div
            style={{
              width: '100%',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              position: 'sticky',
              top: '120px',
              backgroundColor: 'white',
              // '"1"' 형식은 'ZIndex | undefined' 형식에 할당할 수 없습니다.ts(2322)
              // @ts-ignore
              zIndex: '10',
            }}
          >
            <div>
              <Select id="type-select" name="dtype" onChange={onChange}>
                <option value="ONLINE">온라인</option>
                {/* 오프라인은 배포 이후 지원 */}
                <option value="OFFLINE">오프라인</option>
              </Select>
              <Select id="study-select" name="studyType" onChange={onChange}>
                <option value="FREE">무료</option>
                <option value="PAY">유료</option>
              </Select>
              <Select id="call-data-count-select" name="offset" onChange={onChange}>
                <option value={4}>4개</option>
                <option value={8}>8개</option>
                <option value={16}>16개</option>
              </Select>
              <Select id="subject-select" name="subjectId" onChange={onChange}>
                <option id="0" value="ALL">
                  모든 언어
                </option>
                {subjects.map((item: { id: number; name: string }) => (
                  <option key={item.id} id={item.id.toString()} value={item.name}>
                    {item.name}
                  </option>
                ))}
              </Select>
              {inputs.dtype === 'OFFLINE' && (
                <>
                  {/* innerwidth는 한번만 실행되므로 차후에 변경 예정 */}
                  {/* 오프라인은 배포 이후 지원 */}
                  {/* {window.innerWidth < 740 && <br />}
                  <SidoGungu id="city-select" name="sido" onChange={onChange}>
                    <option value="sido1">시도1</option>
                    <option value="sido2">시도2</option>
                  </SidoGungu>
                  <SidoGungu id="city-select" name="gun" onChange={onChange}>
                    <option value="gun1">군1</option>
                    <option value="gun2">군2</option>
                  </SidoGungu>
                  <SidoGungu id="city-select" name="gu" onChange={onChange}>
                    <option value="gu1">구1</option>
                    <option value="gu2">구2</option>
                  </SidoGungu> */}
                </>
              )}
            </div>
            <div>
              <input name="title" onChange={onChange} placeholder="검색" />
            </div>
          </div>
          <ItemList>
            <Items inputs={inputs} modalStateChange={modalStateChange} modal={modal} />
          </ItemList>
        </Section>
      </Main>
      <StudyFooter />
    </>
  );
}

export default OpenStudyView;
