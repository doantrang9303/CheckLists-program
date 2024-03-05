import { useAuth } from "./hooks/useAuth";
export default function callApiAcessToken(){
    const {access_token} = useAuth();
    var xhr = new XMLHttpRequest();
   
    xhr.open('POST', 'http://localhost:9292/test', true);

    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + access_token);
    xhr.send();
}