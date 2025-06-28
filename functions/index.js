/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */


const {setGlobalOptions} = require("firebase-functions");
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendCuriosityNotification = functions.pubsub
    .schedule("every 1 hours")
    .onRun(async (context) => {
      const message = {
        notification: {
          title: "Curiosità del momento",
          body: "Ecco una curiosità fresca per te!",
        },
        topic: "curiosity_topic", // o usa token specifico
      };

      try {
        const response = await admin.messaging().send(message);
        console.log("Notifica inviata:", response);
      } catch (error) {
        console.error("Errore invio notifica:", error);
      }

      return null;
    });

setGlobalOptions({maxInstances: 10});

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
