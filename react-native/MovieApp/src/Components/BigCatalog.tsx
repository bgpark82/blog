import React from 'react';
import {Dimensions} from 'react-native';
import Styled from 'styled-components/native';

const Container = Styled.TouchableOpacity``;
const CatalogImage = Styled.Image``;
const InfoContainer = Styled.View`
  position: absolute;
  bottom: 0;
  width: 100%;
  align-items: flex-start;
`;
const LabelYear = Styled.Text`
  background-color: #E70915;
  color: #FFFFFF;
  padding: 4px 8px;
  margin-left: 4px;
  margin-bottom: 4px;
  font-weight: bold;
  border-radius: 4px;
`;
const SubInfoContainer = Styled.View`
`;
const Background = Styled.View`
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-color: #141414;
  opacity: 0.9;
`;
const LabelTitle = Styled.Text`
  font-size: 18px;
  font-weight: bold;
  color: #FFFFFF;
  padding: 8px 16px 4px 16px;
`;
const LabelGenres = Styled.Text`
  font-size: 12px;
  color: #FFFFFF;
  padding: 4px 16px 8px 16px;
`;

interface Props {
  id: number;
  image: string;
  year: number;
  title: string;
  genres: Array<string>;
  onPress?: (id: number) => void;
}

// 영화 하나하나를 화면에 표시하기 위한 컴포넌트
// 영화 상세 컴포넌트에서도 사용할 예정
const BigCatalog = ({id, image, year, title, genres, onPress}: Props) => {

    const onPressCatalog = () => {
        if (onPress && typeof onPress === 'function') {
            onPress(id);
          }
    }

    return (
      <Container
        activeOpacity={1}
        onPress={onPressCatalog}>
        <CatalogImage
          source={{uri: image}}
          // FlatList에서 가로를 꽉 채우는 스크롤 아이템
          // Dimension : 단말기 화면의 전체 가로사이즈를 가져와 이미지 사이즈에 적용
          style={{width: Dimensions.get('window').width, height: 300}}
        />
        <InfoContainer>
          <LabelYear>{year}년 개봉</LabelYear>
          <SubInfoContainer>
            <Background />
            <LabelTitle>{title}</LabelTitle>
            <LabelGenres>{genres.join(', ')}</LabelGenres>
          </SubInfoContainer>
        </InfoContainer>
      </Container>
    );
  };

export default BigCatalog
