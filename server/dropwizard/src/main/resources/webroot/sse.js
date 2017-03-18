$(function ()
{
    console.log("window load");
    setupServerSentEvents("asd");
});

function setupServerSentEvents(id)
{
    if (typeof(EventSource) === "undefined") {
        //browser does not support SSE
        alert("Your browser does not support Server Sent Events, get Chrome...");
        return;
    }

    console.log("Creating EventSource");
    const eventSource = new EventSource("api/event-stream/");
    eventSource.onmessage = function (event)
    {
        console.log("Got an event");
        console.log(event);
        document.getElementById('events').innerHTML += "New event with id " + event.data + "\n";
    };
    eventSource.onError = function ()
    {
        console.log("onError")
    };
    eventSource.onopen = function ()
    {
        console.log("Connection to server opened.");
    };
    //Tells the source not to reconnect if the server closes the connection
    eventSource.addEventListener('close', function (event)
    {
        console.log("Got the close event");
        eventSource.close();
    }, false);
    console.log("Shit should work now");
}
