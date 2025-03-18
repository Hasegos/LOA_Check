const characterInformation = document.getElementById("characterInformation");


document.addEventListener("DOMContentLoaded", () => {
    const api = document.getElementById("characterSiblings");
    const apiTrim = api.textContent.trim(); // 띄워쓰기 삭제
    const apiJSON = JSON.parse(apiTrim); // string 가져오고

    const apiData = JSON.parse(apiJSON); // 한번더 json 파싱



    // 캐릭터에대한 정보를 넣어줄거야
    // (캐릭터 서버, 이름, 레벨, 직업 이렇게만)
    function charactersInformation() {    // 캐릭터 이름

        const br = document.createElement("br");

        apiData.forEach((item) =>{
            const div = document.createElement("div");

            div.append(item.ServerName , br.cloneNode(),
                item.CharacterClassName , br.cloneNode(),
                item.CharacterName , "  : ", item.ItemAvgLevel ,
            )

            characterInformation.appendChild(div);

        });

         //searchCharacter(characterImformation, characterEquipment,
         //characterEngraving);
    }

    charactersInformation();

 });