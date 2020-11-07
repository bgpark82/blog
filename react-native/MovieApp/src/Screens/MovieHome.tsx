
import React, {useContext, useEffect} from 'react';
import SplashScreen from 'react-native-splash-screen';
import Styled from 'styled-components/native';

import SubCatalogList from './SubCatalogList';
import BigCatalogList from './BigCatalogList';
import AsyncStorage from '@react-native-community/async-storage';
import Button from '~/Components/Button';
// import SubCatalogList from './SubCatalogList';

const Container = Styled.ScrollView`
  flex: 1;
  background-color: #141414;
`;

const StyleButton = Styled.TouchableOpacity`
  padding: 8px;
`;
const Icon = Styled.Image`
`;


const MovieHome = ({navigation}) => {

  

    const logout = () => {
        AsyncStorage.removeItem('key')
        navigation.navigate('LoginNavigator')
    }

  useEffect(() => {
    navigation.setParams({
        logout: logout
    })
  }, []);

  return (
    <Container>
      <BigCatalogList
        url="https://yts.lt/api/v2/list_movies.json?sort_by=like_count&order_by=desc&limit=5"
        onPress={(id: number) => {
          navigation.navigate('MovieDetail', {
            id,
          });
        }}
      />
      <SubCatalogList
        title="최신 등록순"
        url="https://yts.lt/api/v2/list_movies.json?sort_by=date_added&order_by=desc&limit=10"
        onPress={(id: number) => {
          navigation.navigate('MovieDetail', {
            id,
          });
        }}
      />
      <SubCatalogList
        title="평점순"
        url="https://yts.lt/api/v2/list_movies.json?sort_by=rating&order_by=desc&limit=10"
        onPress={(id: number) => {
          navigation.navigate('MovieDetail', {
            id,
          });
        }}
      />
      <SubCatalogList
        title="다운로드순"
        url="https://yts.lt/api/v2/list_movies.json?sort_by=download_count&order_by=desc&limit=10"
        onPress={(id: number) => {
          navigation.navigate('MovieDetail', {
            id,
          });
        }}
      />
    </Container>
  );
};

// MovieHome.navigationOptions = {
//   title: 'MOVIES',
//   headerTransparent: true,
//   headerTintColor: '#E70915',
//   headerTitleStyle: {
//       fontWeight: 'bold'
//   }
// }

MovieHome.navigationOptions = ({navigation}) => {
  const logout = navigation.getParam('logout');
  return {
    title: "MOVIE",
    headerTintColor: "#E70915",
    headerStyle: {
      backgroundColor: '#141414',
      borderBottomWidth:0,
    },
    headerTitleStyle: {
        fontWeight: 'bold'
    },
    headerBackTitle: null,
    headerRight:() =>
      (<Button
      label="logout"
        onPress = {() => {
          if(logout && typeof logout === 'function'){
            logout();
          }
        }}
      >
        {/* <Icon source={require('~/Assets/Images/ic_logout.png')}/> */}
      </Button>)
  }
}

export default MovieHome;
