import AsyncStorage from '@react-native-community/async-storage'
import React from 'react'
import { ActivityIndicator } from 'react-native'
import { NavigationState } from 'react-navigation'
import { NavigationStackProp } from 'react-navigation-stack'
import Styled from 'styled-components/native'

const Container = Styled.View`
    
`

interface Props {
    navigation: NavigationStackProp<NavigationState>
}

const CheckLogin = ({navigation}:Props) => {
    AsyncStorage.getItem('key')
    .then((value) => {
        if(value) {
            // AsyncStorage에서 key가 있으면 MovieNavigator로 이동 
            navigation.navigate('MovieNavigator')
        } else {
            // AsyncStorage에서 key가 없으면 LoginNavigator 이동 
            navigation.navigate('LoginNavigator')
        }
    }).catch((error:Error) => {
        console.log(error)
    })

    return (
        <Container>
            <ActivityIndicator size="large" color="#E70915"/>
        </Container>
    )
}

// 네비게이ㅕㄴ과 직접 연결되는 컴포넌트들은 navigationOptions로 네비게이션에 필요한 속성을 설정할 수 있다
// 여기서는 로딩화면만 필요하므로 header에 null만 사용
CheckLogin.navigationOptions = {
    header: null
}

export default CheckLogin
