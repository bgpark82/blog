import {createSwitchNavigator, createAppContainer} from 'react-navigation';
import {createStackNavigator} from 'react-navigation-stack';
import CheckLogin from '~/Screens/CheckLogin';
import MovieHome from '~/Screens/MovieHome';
import MovieDetail from '~/Screens/MovieDetail';
import Login from '~/Screens/Login';

// 영화 리스트 화면에서 영화의 상세 페이지 보여준다, 영화 리스트 화면 위에 상세 페이지를 Stack 하여 표현
const LoginNavigator = createStackNavigator({
  Login,
});

const MovieNavigator = createStackNavigator({
  MovieHome,
  MovieDetail,
});

// 로그인 여부에 따라 화면을 Switch
// 로그인 네비게이션과 영화 네비게이션을 createSwitchNavigator를 이용해 로그인 여부에 따라 맞는 화면 표시

// Stacks
// 1. 기본 화면으로 CheckLogin 사용
// 2. 로그인 하지 않으면 Login Navigator
// 3. 로그인 했다면 MovieNavigator
const AppNavigator = createSwitchNavigator(
  {
    CheckLogin,
    LoginNavigator,
    MovieNavigator,
  },
  {
    initialRouteName: 'CheckLogin',
  },
);

// 네비게이션을 다루기 위하 state, link 관리
export default createAppContainer(AppNavigator);
