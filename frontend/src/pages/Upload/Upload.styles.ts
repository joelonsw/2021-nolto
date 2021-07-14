import styled from 'styled-components';

import TextArea from 'components/@common/TextArea/TextArea';
import TextButton from 'components/@common/TextButton/TextButton';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 46rem;
  margin-right: auto;
  margin-left: auto;
  padding-top: 7.75rem;
`;

const TitleWrapper = styled.h2`
  margin-bottom: 3rem;
`;

const VerticalWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  border-bottom: 2rem;
  width: 100%;
  margin-bottom: 1rem;
`;

export const ContentTextArea = styled(TextArea)`
  height: 31.25rem;
`;

const InputsContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  margin-bottom: 1rem;

  & > div:first-child {
    width: 90%;
    margin-bottom: 0;
  }
`;

const StretchWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1.5rem;
  border-bottom: 2rem;
  width: 100%;
  margin-bottom: 1rem;

  & > .stretch-label {
    width: 15rem;
  }
`;

export const StyledButton = styled(TextButton.Regular)`
  width: 19.5rem;
  height: 4.25rem;
  font-size: 1.75rem;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 2.75rem;
  margin: 10.25rem 0 5.5rem;
`;

export default {
  Root,
  TitleWrapper,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  ButtonsWrapper,
};