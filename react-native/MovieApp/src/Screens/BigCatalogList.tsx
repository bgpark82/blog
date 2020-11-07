
import React, {useState, useEffect} from 'react';
import {FlatList} from 'react-native';
import Styled from 'styled-components/native';

import BigCatalog from '~/Components/BigCatalog';

const Container = Styled.View`
    height: 300px;
    margin-bottom: 8px;
`;

interface Props {
  url: string;
  onPress: (id: number) => void;
}

// 영화 리스트 API에서 like_count를 내림차순으로 정렬하고 5개를 가져와 화면에 표시
// 영화 아이템을 선택하면 상세 페이지로 이동
const BigCatalogList = ({url, onPress}: Props) => {
  const [data, setData] = useState<Array<IMovie>>([]);

  const getMovies = () => {
    fetch(url)
    .then((response) => response.json())
    .then((json) => {
      console.log(json);
      setData(json.data.movies);
    })
    .catch((error) => {
      console.log(error);
    });
  }

  useEffect(() => {
    getMovies();
  }, []);

  return (
    <Container>
      <FlatList
        horizontal={true}
        pagingEnabled={true}
        data={data}
        keyExtractor={(item, index) => {
          return `bigScreen-${index}`;
        }}
        renderItem={({item, index}) => (
          <BigCatalog
            id={(item as IMovie).id}
            image={(item as IMovie).large_cover_image}
            year={(item as IMovie).year}
            title={(item as IMovie).title}
            genres={(item as IMovie).genres}
            onPress={onPress}
          />
        )}
      />
    </Container>
  );
};

export default BigCatalogList;

