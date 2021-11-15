import React, { ChangeEvent, useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { sendMessage } from '../ToolKit/messages';
import StudyHeader from '../components/StudyHeader';
import { Button, Input, InputTitle, InputWrap, Main, Section, TextAreaInput } from '../elements';
import { PayloadSuccessType, ResSendMessage, ResSendMessageButton } from '../ToolKit/axiosType';
import { AppDispatch } from '../store';

interface MessageViewProps {}

function MessageView({}: MessageViewProps) {
  const history = useHistory();
  const Dispatch: AppDispatch = useDispatch();

  const [inputs, setInputs] = useState({
    email: '',
  });
  const [textInputs, setTextInputs] = useState({
    content: '',
  });

  useEffect(() => {
    // queryString
    const sliceQS = history.location.search.slice(7);

    setInputs({
      email: sliceQS,
    });
  });

  const { email } = inputs;
  const { content } = textInputs;
  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };
  const onChangeText = (e: any) => {
    const { name, value } = e.target;
    setTextInputs({
      ...textInputs,
      [name]: value,
    });
  };

  const onClickMessage = async () => {
    const { email } = inputs;
    const { content } = textInputs;
    const obj = {
      email,
      content,
    };
    //ResSendMessageButton 타입 넣는거 도와주세요 help
    const dispatch: any = await Dispatch(sendMessage(obj));
    try {
      const statusCode = dispatch.payload.payload.status;
      if (statusCode === 201) {
        history.goBack();
      }
    } catch {
      const notUser = dispatch.payload.message;
      alert(notUser);
    }
  };

  return (
    <>
      <StudyHeader>메세지 보내기</StudyHeader>
      <Main>
        <Section>
          <InputWrap>
            <InputTitle htmlFor="email">Message To:</InputTitle>
            <Input
              placeholder="받는사람을 입력하세요"
              id="email"
              name="email"
              type="email"
              value={email}
              onChange={onChange}
            />
          </InputWrap>
          <InputWrap>
            <InputTitle placeholder="받는사람을 입력하세요">Contents</InputTitle>
            <TextAreaInput
              placeholder="본문을 입력하세요"
              id="content"
              name="content"
              value={content}
              onChange={onChangeText}
            />
          </InputWrap>
          <Button style={{ marginTop: '250px', backgroundColor: '#51aafe' }} onClick={onClickMessage}>
            메세지 보내기
          </Button>
        </Section>
      </Main>
    </>
  );
}

export default MessageView;
