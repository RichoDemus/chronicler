$(function () {
    console.log("window load");
    setupServerSentEvents();
});

function setupServerSentEvents() {
    if (typeof(EventSource) === "undefined") {
        //browser does not support SSE
        alert("Your browser does not support Server Sent Events, get Chrome...");
        return;
    }

    const eventSource = new EventSource("api/event-stream/");
    eventSource.onmessage = function (event) {

        const textArea = document.getElementById('events');
        textArea.innerHTML += "New event with id " + event.data + "\n";
        textArea.scrollTop = textArea.scrollHeight;

    };
    eventSource.onError = function () {
        console.log("onError")
    };
    eventSource.onopen = function () {
        console.log("Connection to server opened.");
    };
    //Tells the source not to reconnect if the server closes the connection
    eventSource.addEventListener('close', function (event) {
        console.log("Got the close event");
        eventSource.close();
    }, false);
}

function createAndSendEvent() {
    const postData = {};
    postData.id = guidGenerator();
    postData.data = Math.random().toString(36).substr(2, 5);
    $.ajax({
        url: "api/events",
        contentType: "application/json",
        data: JSON.stringify(postData),
        type: "POST"
    });
}
// credits to http://stackoverflow.com/a/6860916/78679
function guidGenerator() {
    const S4 = function () {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    };
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}
