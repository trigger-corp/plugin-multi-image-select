//
//	gallerypicker_API.h
//	ForgeInspector
//
//	Created by Antoine van Gelder on 27/11/2012.
//	Copyright (c) 2012 Trigger Corp. All rights reserved.
//


#import <ELCImagePickerFramework/ELCImagePickerController.h>
#import <ELCImagePickerFramework/ELCAlbumPickerController.h>


// ImagePickerDelegate
@interface ImagePickerDelegate : UIViewController <ELCImagePickerControllerDelegate> {
	ForgeTask *task;
}
- (ImagePickerDelegate*) initWithTask:(ForgeTask*)task;
@end


// gallerypicker_API
@interface gallerypicker_API : NSObject {
	ImagePickerDelegate *pickerDelegate;
}
+ (void) getImages:(ForgeTask*)task;
@end


// AppDelegate
@interface AppDelegate : NSObject <UIApplicationDelegate> {
	ImagePickerDelegate *viewController;
}
@property (nonatomic, retain) IBOutlet ImagePickerDelegate *viewController;
@end


