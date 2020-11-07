import AsyncStorage from '@react-native-community/async-storage'
import React from 'react'
import { Linking } from 'react-native'
import Styled from 'styled-components/native'
import Button from '~/Components/Button'
import Input from '~/Components/Input'


const Container = Styled.SafeAreaView`
    flex:1;
    background-color: #141414;
    align-items:center;
    justify-content: center;
`
const FormContainer = Styled.View``
const PasswordReset = Styled.Text`
    color: #FFFFFF;
`


const Login = ({navigation}) => {

    const onPressLogin = () => {
        AsyncStorage.setItem('key','JWT_KEY');
        navigation.navigate('MovieNavigator')
    }

    const onPressReset = () => {
        // 웹 브라우저와 연결하기 위한 Linking 컴포넌트
        Linking.openURL('https://dev-yakuza.github.io/ko/')
    }

    return (
        <Container>
            <FormContainer>
                <Input 
                  placeholder="이메일"
                />
                <Input 
                    secureTextEntry={true}
                    placeholder="비밀번호"
                />
                <Button label="로그인" onPress={onPressLogin}/>
                <PasswordReset onPress={onPressReset}>비밀번호 재설정</PasswordReset>
            </FormContainer>
        </Container>
    )
}

// 투명 네비게이션 헤더 
Login.navigationOptions = {
    title: 'LOGIN',
    headerTransparent: true,
    headerTintColor: '#E70915',
    headerTitleStyle: {
        fontWeight: 'bold'
    }
}

export default Login
